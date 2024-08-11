package com.example.backend.controller;

import com.example.backend.dto.CategoryDto;
import com.example.backend.dto.UserDto;
import com.example.backend.security.principals.CustomUserDetails;
import com.example.backend.service.ICategoryService;
import com.example.backend.service.IUserService;
import com.example.backend.service.IWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
