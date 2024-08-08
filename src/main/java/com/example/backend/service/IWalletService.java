package com.example.backend.service;

import com.example.backend.dto.WalletDto;
import com.example.backend.dto.WalletInfoDto;
import com.example.backend.dto.response.ResponseSuccess;
import com.example.backend.model.entity.Wallet;
import com.example.backend.security.principals.CustomUserDetails;

import java.util.Set;

import java.util.List;

public interface IWalletService {
    ResponseSuccess saveWallet(WalletDto walletDto, CustomUserDetails customUserDetails);

    ResponseSuccess updateWallet(Long id, WalletDto walletDto);

    WalletDto findWalletById(Long id);

    void deleteWalletByID(Long id);

    void updateWalletAmount(Long id, WalletDto walletDto);

    void shareWallet(Long id, String email, String roleName);

    void addNewWalletRole(Long id,String roleName);

    Set<WalletInfoDto> findAllWalletByUserId(Long id);


}
