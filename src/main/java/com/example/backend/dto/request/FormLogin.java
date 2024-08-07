package com.example.backend.dto.request;

import com.example.backend.validator.Regex;
import com.example.backend.validator.ValidationMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FormLogin {
    @NotBlank(message = ValidationMessage.NOT_NULL)
    @Email(message = ValidationMessage.ERR_REGEX_EMAIL)
    private String email;
    @Pattern(regexp = Regex.REGEX_PASS, message = ValidationMessage.ERR_REGEX_PASS)
    @NotBlank(message = ValidationMessage.NOT_NULL)
    private String password;
}
