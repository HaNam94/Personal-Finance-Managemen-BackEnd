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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements IWalletService {
    @Autowired
    private IWalletRepo walletRepo;
    @Autowired
    private IUserRepo userRepository;

    @Override
    public Set<WalletInfoDto> findAllWalletByUserId(Long id) {
        return walletRepo.findAllByUserId(id);
    }

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

        return walletRepo.save(wallet);
    }

    @Override
    public WalletInfoDto getWalletWithPermission(Long walletId, Long userId) {
        return walletRepo.findWalletByIdAndUserId(walletId, userId);
    }

    @Override
    public boolean isOwner(Long walletId, Long userId) {
        return walletRepo.isOwner(walletId, userId);
    }

    @Override
    public ResponseSuccess updateWallet(Long id, WalletDto walletDto) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new RuntimeException("Wallet not found"));
    public void updateWallet(Long id, WalletDto walletDto) {
        Wallet wallet = walletRepo.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy ví này"));
        wallet.setWalletName(walletDto.getWalletName());
        wallet.setIcon(walletDto.getIcon());
        wallet.setWalletDescription(walletDto.getWalletDescription());
        wallet.setCurrency(walletDto.getCurrency());
        wallet.setAmount(walletDto.getAmount());
        walletRepo.save(wallet);
    }

    @Override
    public WalletDto findWalletById(Long id) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new RuntimeException("Wallet not found"));
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
    public void deleteWalletByID(Long id) {
        walletRepository.deleteWalletByID(id);
    }

    @Override
    public void updateWalletAmount(Long id, WalletDto walletDto) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new RuntimeException("Wallet not found"));
        BigDecimal newAmount = wallet.getAmount().add(walletDto.getAmount());
        walletRepository.updateWalletAmount(id, newAmount);

    }

    @Override
    public void shareWallet(Long id, String email, String roleName) {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new RuntimeException("Wallet not found"));
        Set<User> users = wallet.getUsers();
        users.add(user);
        wallet.setUsers(users);
        wallet.setWalletRole(roleName);
        walletRepository.save(wallet);
    }

    @Override
    public void addNewWalletRole(Long id, String roleName) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new RuntimeException("Wallet not found"));
        wallet.setWalletRole(roleName);
        walletRepository.save(wallet);
    }


    @Override
    public Set<WalletInfoDto> findAllWalletByUserId(Long id) {
        return walletRepository.findAllWalletByUserId(id);
    }
}
