package com.example.backend.service;

import com.example.backend.dto.UserDto;
import com.example.backend.dto.request.FormLogin;
import com.example.backend.dto.request.FormRegister;
import com.example.backend.dto.response.ResponseSuccess;
import com.example.backend.dto.response.ResponseUser;
import org.springframework.stereotype.Repository;
import org.springframework.validation.BindingResult;
@Repository
public interface UserService {
    ResponseSuccess register(FormRegister formRegister, BindingResult bindingResult);
    ResponseUser login(FormLogin formLogin, BindingResult bindingResult) ;

    UserDto findById(Long id);
}
