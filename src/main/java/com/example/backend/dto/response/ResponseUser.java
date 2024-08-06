package com.example.backend.dto.response;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResponseUser {
    private String email;
    private Boolean userStatus;
    private String avatar;
    private String fullName;
    private String phone;
    private LocalDate birthday;
    private String otpCode;
    private Boolean isDelete;
    private Collection<? extends GrantedAuthority> authorities;
    private String accessToken;
}
