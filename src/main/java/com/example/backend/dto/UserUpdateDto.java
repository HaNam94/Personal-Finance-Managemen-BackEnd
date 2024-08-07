package com.example.backend.dto;

import com.example.backend.validator.Regex;
import com.example.backend.validator.ValidationMessage;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserUpdateDto {
    private String username;
    private MultipartFile avatar;
    @Past(message = ValidationMessage.ERR_DATE_PAST)
    private LocalDate dob;
    @Pattern(regexp = Regex.REGEX_PHONE, message = ValidationMessage.ERR_REGEX_PHONE)
    private String phone;
    private String currentPassword;
    @Pattern(regexp = Regex.REGEX_PASS, message = ValidationMessage.ERR_REGEX_PASS)
    private String newPassword;
    private String confirmPassword;
}
