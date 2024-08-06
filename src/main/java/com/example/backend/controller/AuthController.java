package com.example.backend.controller;

import com.example.backend.dto.request.FormLogin;
import com.example.backend.dto.request.FormRegister;
import com.example.backend.dto.response.ResponseSuccess;
import com.example.backend.dto.response.ResponseUser;
import com.example.backend.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {

    private final UserServiceImpl userService;



    @PostMapping("/public/login")
    public ResponseEntity<?> login(@Valid @RequestBody FormLogin formLogin,
                             BindingResult bindingResult) {
      ResponseUser responseUser =  userService.login(formLogin, bindingResult);
        return new ResponseEntity<>(responseUser, HttpStatus.OK);
    }
    @PostMapping("/public/register")
    public ResponseEntity<?> register(@Valid @RequestBody FormRegister formRegister, BindingResult result) {
        ResponseSuccess responseSuccess = userService.register(formRegister, result);
            return new ResponseEntity<>(responseSuccess, HttpStatus.OK);

    }


}
