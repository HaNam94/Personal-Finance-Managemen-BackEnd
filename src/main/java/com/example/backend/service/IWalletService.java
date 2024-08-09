package com.example.backend.service;

import com.example.backend.dto.WalletDto;
import com.example.backend.dto.WalletInfoDto;
import com.example.backend.dto.response.ResponseSuccess;
import com.example.backend.model.entity.Wallet;
import com.example.backend.security.principals.CustomUserDetails;

import java.math.BigDecimal;
import java.util.Set;

import java.util.List;

public interface IWalletService {
    void saveWallet(Long ownerId, WalletDto walletDto);

    void updateWallet(Long walletId, WalletDto walletDto);

    WalletDto findWalletById(Long id);

    void deleteWalletById(Long id);

    WalletInfoDto getWalletWithPermission(Long walletId, Long userId);

    boolean isOwner(Long walletId, Long userId);

    void updateWalletAmount(Long id, BigDecimal newAmount);


    Set<WalletInfoDto> findAllWalletByUserId(Long id);

    void shareWallet(Long walletId, String email, String walletRoleName);

}
