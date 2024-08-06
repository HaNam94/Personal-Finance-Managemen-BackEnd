package com.example.backend.advice;

import com.example.backend.exception.CustomValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandlerExceptionAPI {





    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(CustomValidationException e) {
        return new ResponseEntity<>(e.getErrors(), HttpStatus.BAD_REQUEST);
    }



//    @ExceptionHandler(javax.naming.AuthenticationException.class)
//    public ResponseEntity<?> handleAuthenticationException(AuthenticationException e) {
//        Map<String, String> map = new HashMap<>();
//        map.put("login", e.getMessage());
//        return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
//    }
}
