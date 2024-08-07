package com.example.backend.service;

import com.example.backend.dto.WalletDto;
import com.example.backend.dto.response.ResponseSuccess;
import com.example.backend.model.entity.Wallet;
import com.example.backend.security.principals.CustomUserDetails;

public interface IWalletService {
    ResponseSuccess saveWallet(WalletDto wallet, CustomUserDetails customUserDetails);
    ResponseSuccess updateWallet(Long id, WalletDto walletDto);
    WalletDto findWalletById(Long id);
    void deleteWalletByID(Long id);
}
