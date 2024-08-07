package com.example.backend.service;

import com.example.backend.dto.UserDto;
import com.example.backend.dto.UserUpdateDto;
import com.example.backend.dto.request.FormLogin;
import com.example.backend.dto.request.FormRegister;
import com.example.backend.dto.response.ResponseSuccess;
import com.example.backend.dto.response.ResponseUser;
import com.example.backend.security.principals.CustomUserDetails;
import org.springframework.validation.BindingResult;

public interface IUserService {
    ResponseSuccess register(FormRegister formRegister, BindingResult bindingResult);
    ResponseUser login(FormLogin formLogin, BindingResult bindingResult) ;

    UserDto findUserByEmail(String email);

    void updateUser(CustomUserDetails userDetails, UserUpdateDto userUpdateDto);
    ResponseSuccess deleteUserById(Long id);
}
