package com.example.backend.dto.request;

import com.example.backend.validator.Regex;
import com.example.backend.validator.ValidationMessage;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FormRegister {
    @NotBlank(message = ValidationMessage.NOT_NULL)
    private String username;
    @Email(message = ValidationMessage.ERR_REGEX_EMAIL)
    @NotNull(message = ValidationMessage.NOT_NULL)
    private String email;
    @NotNull(message = ValidationMessage.NOT_NULL)
    @Pattern(regexp = Regex.REGEX_PASS, message = ValidationMessage.ERR_REGEX_PASS)
    private String password;
    private String confirmPassword;
}
