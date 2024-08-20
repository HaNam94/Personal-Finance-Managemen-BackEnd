package com.example.backend.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder

public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String budgetName;
    private BigDecimal budgetAmount;
    private String budgetDescription;
    private LocalDate budgetDate;
    private String currency = "VND";
    @OneToOne(fetch = FetchType.EAGER)
    private Category category;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
}
