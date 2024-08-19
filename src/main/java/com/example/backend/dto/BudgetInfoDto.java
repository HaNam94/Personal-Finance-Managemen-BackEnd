package com.example.backend.dto;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BudgetInfoDto {
    Long getId();
    String getBudgetName();
    BigDecimal getBudgetAmount();
    String getBudgetDescription();
    LocalDate getBudgetDate();
    @Value("#{target.category.transactions}")
    List<TransactionInfoDto> getTransactions();
}
