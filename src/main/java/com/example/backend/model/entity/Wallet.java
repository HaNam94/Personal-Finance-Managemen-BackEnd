package com.example.backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String walletName;
    private String icon;
    private String walletDescription;
    private BigDecimal currency;
    private Integer amount;
    private Integer walletStatus;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> users;
    @ManyToMany
    private Set<Role> roles;

}
