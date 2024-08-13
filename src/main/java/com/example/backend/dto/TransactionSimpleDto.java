package com.example.backend.dto;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface TransactionSimpleDto {
    Long getId();
    String getNote();
    BigDecimal getAmount();
    LocalDate getDatetime();
    @Value("#{target.category.icon}")
    String getIcon();

    @Value("#{target.category.categoryName}")
    String getCategoryName();
}
