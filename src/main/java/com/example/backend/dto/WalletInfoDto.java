package com.example.backend.dto;

import com.example.backend.validator.ValidationMessage;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public interface WalletInfoDto {
    Long getId();
    String getWalletName();
    String getIcon();
    String getWalletDescription();
    String getCurrency();
    BigDecimal getAmount();
}
