package com.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface TransactionInfoDto {
    Long getId();
    String getNote();
    BigDecimal getAmount();
    Boolean getIsTransfer();
//    @JsonFormat(pattern="dd-MM-yyyy")
    LocalDate getDatetime();
    @Value("#{target.category.icon}")
    String getIcon();
    @Value("#{target.category.categoryType}")
    Integer getCategoryType();
    @Value("#{target.category.categoryName}")
    String getCategoryName();
    @Value("#{target.wallet.walletName}")
    String getWalletName();
    @Value("#{target.category.id}")
    Long getCategoryId();
    @Value("#{target.wallet.id}")
    Long getWalletId();
}
