package com.example.backend.service.impl;

import com.example.backend.model.dto.UserDto;
import com.example.backend.repository.IUserRepo;
import com.example.backend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepo userRepo;

    @Override
    public Optional<UserDto> findById(Long id) {
        return userRepo.findUserById(id);
    }
}
