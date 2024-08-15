package com.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface TransactionInfoDto {
    Long getId();
    String getNote();
    BigDecimal getAmount();
//    @JsonFormat(pattern="dd-MM-yyyy")
    LocalDate getDatetime();
    String getIcon();
    Integer getCategoryType();
    String getCategoryName();
    String getWalletName();
    Long getCategoryId();
    Long getWalletId();
}
