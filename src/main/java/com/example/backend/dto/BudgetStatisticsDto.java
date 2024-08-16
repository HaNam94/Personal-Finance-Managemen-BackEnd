package com.example.backend.dto;

import com.example.backend.model.entity.Transaction;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BudgetStatisticsDto {
    private BigDecimal remainingAmount;
    private List<TransactionInfoDto> transactions;
}
