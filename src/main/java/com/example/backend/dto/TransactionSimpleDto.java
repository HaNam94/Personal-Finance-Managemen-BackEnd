package com.example.backend.dto;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface TransactionSimpleDto {
    Long getId();
    String getNote();
    BigDecimal getAmount();
    Boolean getIsTransfer();
    LocalDate getDatetime();
    @Value("#{target.category.icon}")
    String getIcon();

    @Value("#{target.category.categoryName}")
    String getCategoryName();
    @Value("#{target.category.categoryType}")
    Integer getCategoryType();
    @Value("#{target.wallet.walletName}")
    String getWalletName();
    @Value("#{target.wallet.currency}")
    String getWalletCurrency();
;}
