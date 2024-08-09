package com.example.backend.service.impl;

import com.example.backend.dto.WalletDto;
import com.example.backend.dto.WalletInfoDto;
import com.example.backend.model.WalletRole;
import com.example.backend.model.entity.User;
import com.example.backend.model.entity.Wallet;
import com.example.backend.model.entity.WalletUserRole;
import com.example.backend.repository.IUserRepo;
import com.example.backend.repository.IWalletRepo;
import com.example.backend.service.IWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements IWalletService {
    @Autowired
    private IWalletRepo walletRepository;
    @Autowired
    private IUserRepo userRepository;



    @Override
    public Wallet saveWallet(Long ownerId, WalletDto walletDto) {

        User owner = userRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));

        Wallet wallet = Wallet.builder()
                .walletName(walletDto.getWalletName())
                .icon(walletDto.getIcon())
                .walletDescription(walletDto.getWalletDescription())
                .currency(walletDto.getCurrency())
                .amount(walletDto.getAmount())
                .walletStatus(true)
                .build();

        WalletUserRole walletUserRole = new WalletUserRole();
        walletUserRole.setUser(owner);
        walletUserRole.setWallet(wallet);
        walletUserRole.setRole(WalletRole.OWNER);

        if (wallet.getWalletRoles() == null) {
            wallet.setWalletRoles(new HashSet<>());
        }

        if (owner.getWalletRoles() == null) {
            owner.setWalletRoles(new HashSet<>());
        }

        wallet.getWalletRoles().add(walletUserRole);
        owner.getWalletRoles().add(walletUserRole);

        return walletRepository.save(wallet);
    }

    @Override
    public WalletInfoDto getWalletWithPermission(Long walletId, Long userId) {
        return walletRepository.findWalletByIdAndUserId(walletId, userId);
    }

    @Override
    public boolean isOwner(Long walletId, Long userId) {
        return walletRepository.isOwner(walletId, userId);
    }

    @Override
    public void updateWalletAmount(Long id, BigDecimal newAmount) {
    Wallet wallet = walletRepository.findById(id).orElseThrow(()-> new RuntimeException("Khong tim thay vi"));
    wallet.setAmount(newAmount);
    walletRepository.save(wallet);
    }

    @Override
    public Wallet updateWallet(Long walletId, WalletDto walletDto) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new RuntimeException("Không tìm thấy ví này"));

            wallet.setWalletName(walletDto.getWalletName());
            wallet.setIcon(walletDto.getIcon());
            wallet.setWalletDescription(walletDto.getWalletDescription());
            wallet.setCurrency(walletDto.getCurrency());
            wallet.setAmount(walletDto.getAmount());
            walletRepository.save(wallet);


        return wallet;
    }


    @Override
    public WalletDto findWalletById(Long walletId) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new RuntimeException("Wallet not found"));

        WalletDto walletDto = WalletDto.builder()
                .walletName(wallet.getWalletName())
                .icon(wallet.getIcon())
                .walletDescription(wallet.getWalletDescription())
                .currency(wallet.getCurrency())
                .amount(wallet.getAmount())
                .id(wallet.getId())
                .build();
        return walletDto;
    }

    @Override
    public void deleteWalletById(Long walletId) {
            walletRepository.deleteById(walletId);
    }



    @Override
    public Set<WalletInfoDto> findAllWalletByUserId(Long id) {
        return walletRepository.findAllByUserId(id);
    }
    @Override
    public void shareWallet(Long walletId, String email, String walletRoleName) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new RuntimeException("Không tìm thấy ví này"));
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));

        WalletUserRole walletUserRole = new WalletUserRole();
        walletUserRole.setUser(user);
        walletUserRole.setWallet(wallet);
        walletUserRole.setRole(WalletRole.valueOf(walletRoleName));
        wallet.getWalletRoles().add(walletUserRole);
        walletRepository.save(wallet);
    }
}
