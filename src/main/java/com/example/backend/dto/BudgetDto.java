package com.example.backend.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BudgetDto {
    private Long id;
    private String budgetName;
    private BigDecimal budgetAmount;
    private String budgetDescription;
    private Long categoryId;
}
