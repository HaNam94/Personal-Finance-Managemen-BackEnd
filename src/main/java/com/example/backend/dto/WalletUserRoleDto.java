package com.example.backend.dto;

import com.example.backend.model.WalletRole;
import org.springframework.beans.factory.annotation.Value;

public interface WalletUserRoleDto {
    Long getId();
    WalletRole getRole();
    @Value("#{target.user.id}")
    Long getUserId();
}