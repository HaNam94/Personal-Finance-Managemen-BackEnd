package com.example.backend.repository;

import com.example.backend.model.entity.WalletUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IWalletUserRolesRepo extends JpaRepository<WalletUserRole,Long> {
    WalletUserRole findWalletUserRoleByWalletIdAndUserId(Long walletId , Long userId);
}
