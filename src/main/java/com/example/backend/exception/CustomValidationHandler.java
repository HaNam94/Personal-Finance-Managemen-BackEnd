package com.example.backend.exception;

import lombok.*;

import java.util.Map;
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class CustomValidationHandler extends RuntimeException {
    private Map<String, String> errors;

}
