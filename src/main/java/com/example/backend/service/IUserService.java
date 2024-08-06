package com.example.backend.service;

import com.example.backend.dto.request.FormLogin;
import com.example.backend.dto.request.FormRegister;
import com.example.backend.dto.response.ResponseSuccess;
import com.example.backend.dto.response.ResponseUser;
import org.springframework.validation.BindingResult;

public interface IUserService {
    ResponseSuccess register(FormRegister formRegister, BindingResult bindingResult);
    ResponseUser login(FormLogin formLogin, BindingResult bindingResult) ;
}
