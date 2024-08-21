package com.example.backend.dto;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BudgetInfoDto {
    Long getId();
    String getBudgetName();
    String getCurrency();
    BigDecimal getBudgetAmount();
    String getBudgetDescription();
    LocalDate getBudgetDate();
    @Value("#{target.category.categoryName}")
    String getCategoryName();
    @Value("#{target.category.icon}")
    String getCategoryIcon();
    @Value("#{target.category.id}")
    Long getCategoryId();
    @Value("#{target.category.transactions}")
    List<TransactionInfoDto> getTransactions();
}
