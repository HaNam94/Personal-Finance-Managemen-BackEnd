package com.example.backend.model.dto;

import java.time.LocalDate;

public interface UserDto {
    Long getId();
    String getEmail();
    String getAvatar();
    LocalDate getDob();
    String getPhone();
}
