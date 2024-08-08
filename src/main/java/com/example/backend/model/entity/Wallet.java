package com.example.backend.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String walletName;
    private String icon;
    private String walletDescription;
    private String currency;
    private BigDecimal amount;
    private Boolean walletStatus;
//    @ManyToMany(fetch = FetchType.EAGER)
//    private Set<User> users;
//    private String walletRole;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WalletUserRole> walletRoles = new HashSet<>();

}
