package com.example.backend.dto.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResponseSuccess {
    private HttpStatus status;
    private String message;
}
