    package com.example.backend.model.entity;

    import jakarta.persistence.*;
    import lombok.*;

    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.util.HashSet;
    import java.util.Set;

    @Entity
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String username;
        private String email;
        private String password;
        private String avatar;
        private String phone;
        private LocalDate dob;
        private Boolean isAccountGoogle;
        private Boolean isDelete;
        private Boolean userStatus;
        private Boolean isActive;
        private String otpCode;
        private LocalDateTime otpGenerateTime;
        private String resetToken;
        private String resetTokenValidAt;
        @ManyToMany(fetch = FetchType.EAGER)
        private Set<Role> roles;

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        private Set<WalletUserRole> walletRoles = new HashSet<>();

        @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
        private Set<Category> categories = new HashSet<>();

        @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
        private Setting setting;
    }
