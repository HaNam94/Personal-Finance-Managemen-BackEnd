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
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String note;
    private BigDecimal amount;
    private LocalDate datetime;
    @ManyToOne
    private Category category;
    @ManyToOne
    private Wallet wallet;
    @ManyToOne
    private User user;

}
