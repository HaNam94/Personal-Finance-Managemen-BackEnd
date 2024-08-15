package com.example.backend.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class TransactionDto {
    private Long id;
    private String note;
    private BigDecimal amount;
    private LocalDate datetime;
    private Integer categoryType;
    private Long categoryId;
    private Long walletId;

}
