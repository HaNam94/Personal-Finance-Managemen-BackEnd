package com.example.backend.controller;

import com.example.backend.dto.UserDto;
import com.example.backend.dto.UserUpdateDto;
import com.example.backend.security.principals.CustomUserDetails;
import com.example.backend.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UserController {


    private final IUserService userService;

    @GetMapping("/me")
    private ResponseEntity<UserDto> findById(
            Authentication authentication
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserDto userDto = userService.findUserByEmail(userDetails.getUsername());
        if(userDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userDto);
    }


    @PutMapping("/me")
    public ResponseEntity<?> update(
            @Validated @RequestBody UserUpdateDto userUpdateDto,
            BindingResult bindingResult,
            Authentication authentication
    ) {
        if (bindingResult.hasFieldErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        userService.updateUser(userDetails, userUpdateDto);
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    @PutMapping("/me/upload-avatar")
    public ResponseEntity<?> uploadAvatar(
            @Validated @ModelAttribute UserUpdateDto userUpdateDto,
            Authentication authentication
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        userService.updateUser(userDetails, userUpdateDto);
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

}
