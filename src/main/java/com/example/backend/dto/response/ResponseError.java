package com.example.backend.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ResponseError {
    private HttpStatus status;
    private String message;
    private Object detail;
}
