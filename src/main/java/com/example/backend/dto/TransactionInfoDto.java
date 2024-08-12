package com.example.backend.dto;

import com.example.backend.model.entity.Category;
import com.example.backend.model.entity.Wallet;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface TransactionInfoDto {
    Long getId();
    String getNote();
    BigDecimal getAmount();
    LocalDate getDatetime();
    String getIcon();
    Integer getCategoryType();
    String getCategoryName();
    String getWalletName();

}
