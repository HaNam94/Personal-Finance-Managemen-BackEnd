package com.example.backend.dto;

import com.example.backend.model.entity.Setting;

import java.time.LocalDate;

public interface UserDto {
    Long getId();

    String getUsername();

    String getEmail();

    String getAvatar();

    LocalDate getDob();

    String getPhone();

    Setting getSetting();
}