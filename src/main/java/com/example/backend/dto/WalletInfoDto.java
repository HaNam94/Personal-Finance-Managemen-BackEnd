package com.example.backend.dto;

import com.example.backend.model.WalletRole;
import com.example.backend.model.entity.WalletUserRole;
import com.example.backend.validator.ValidationMessage;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public interface WalletInfoDto {
    Long getId();
    String getWalletName();
    Boolean getWalletStatus();
    String getIcon();
    String getWalletDescription();
    String getCurrency();
    BigDecimal getAmount();
    Set<WalletUserRoleDto> getWalletRoles();
}
