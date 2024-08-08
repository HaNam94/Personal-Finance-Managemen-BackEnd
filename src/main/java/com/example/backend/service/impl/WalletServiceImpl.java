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
    public boolean isOwner(Long id, Long userId) {
        // TODO: Implement logic to check if user is owner of the wallet,
        // fix cái này nha quyền owner mới được cập nhật
        return true;
    }

    @Override
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
    public void deleteWallet(Long id) {
        walletRepo.deleteById(id);
    }
}
