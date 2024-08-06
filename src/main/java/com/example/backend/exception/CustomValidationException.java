package com.example.backend.exception;

import java.util.Map;

public class CustomValidationException extends RuntimeException {
    private Map<String,String> errors;
    public CustomValidationException(Map<String, String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
