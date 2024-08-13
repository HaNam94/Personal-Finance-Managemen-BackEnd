package com.example.backend.controller;

import com.example.backend.dto.CategoryDto;
import com.example.backend.dto.CategoryFormDto;
import com.example.backend.dto.UserDto;
import com.example.backend.security.principals.CustomUserDetails;
import com.example.backend.service.ICategoryService;
import com.example.backend.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;
    private final IUserService userService;

    @RequestMapping("")
    public ResponseEntity<?> getAll(
            Authentication authentication
    ) {
        UserDto userDto = getUserDto(authentication);
        Iterable<CategoryDto> categories = categoryService.findAllMasterCategoryByUserId(userDto.getId());
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> createCategory(
            @Validated @RequestBody CategoryFormDto categoryFormDto,
            Authentication authentication
    ) {
        UserDto userDto = getUserDto(authentication);
        categoryService.saveCategory(categoryFormDto, userDto.getId());
        return new ResponseEntity<>("{}", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        System.out.println(id);
        System.out.println("=======");
        UserDto userDto = getUserDto(authentication);
        CategoryDto categoryDto = categoryService.findCategoryById(id);
        if (categoryDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!categoryDto.getUserId().equals(userDto.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @Validated @RequestBody CategoryFormDto categoryFormDto,
            Authentication authentication) {
        categoryService.updateCategory(id, categoryFormDto);
        return new ResponseEntity<>("{}", HttpStatus.OK);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UserDto userDto = getUserDto(authentication);
        categoryService.deleteCategory(id, userDto.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    private UserDto getUserDto(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userService.findUserByEmail(userDetails.getUsername());
    }
}
