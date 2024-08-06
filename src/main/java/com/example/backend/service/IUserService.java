package com.example.backend.service;

import com.example.backend.model.dto.UserDto;

import java.util.Optional;

public interface IUserService {
    Optional<UserDto> findById(Long id);
}
