package com.example.backend.dto;

import java.time.LocalDate;

public interface UserDto {
    Long getId();
    String getUsername();
    String getEmail();
    String getAvatar();
    LocalDate getDob();
    String getPhone();
}
