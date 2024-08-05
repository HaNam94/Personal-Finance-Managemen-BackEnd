package com.example.backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String avatar;
    private LocalDate dob;
    private String phone;
    private String otpCode;
    private String otpCodeVerifed;
    private String resetToken;
    private String resetTokenValidAt;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
}
