package com.example.backend.dto;

import com.example.backend.model.entity.Category;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface TransactionInfoDto {
    Long getId();
    String getNote();
    BigDecimal getAmount();
    LocalDate getDatetime();
    Category getCategory();
    WalletDto getWallet();
}
