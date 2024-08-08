package com.example.backend.service;

import com.example.backend.dto.WalletDto;
import com.example.backend.dto.WalletInfoDto;
import com.example.backend.model.entity.Wallet;

import java.util.Set;

public interface IWalletService {

    Set<WalletInfoDto> findAllWalletByUserId(Long id);

    Wallet saveWallet(Long id, WalletDto walletDto);

    WalletInfoDto getWalletWithPermission(Long id, Long id1);

    boolean isOwner(Long id, Long id1);

    void updateWallet(Long id, WalletDto walletDto);

    void deleteWallet(Long id);
}
