package com.example.backend.service.impl;

import com.example.backend.repository.IWalletRepo;
import com.example.backend.service.IWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements IWalletService {
    private final IWalletRepo walletRepository;

    @Override
    public void deleteWalletByID(Long id) {
        walletRepository.deleteWalletByID(id);
    }
}
