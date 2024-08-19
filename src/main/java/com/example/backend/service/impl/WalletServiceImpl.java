package com.example.backend.service.impl;

import com.example.backend.dto.WalletDto;
import com.example.backend.dto.WalletInfoDto;
import com.example.backend.model.WalletRole;
import com.example.backend.model.entity.*;
import com.example.backend.repository.*;
import com.example.backend.service.IWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements IWalletService {

    private final IWalletRepo walletRepository;

    private final IUserRepo userRepository;
    private final ICategoryRepo categoryRepository;
    private final ITransactionRepo transactionRepository;
    private final IWalletUserRolesRepo walletUserRolesRepo;


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

        wallet = walletRepository.save(wallet);

        if(wallet.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            createTransaction(wallet, ownerId, wallet.getAmount(), "income", false, "Khoảng thu ban đầu của ví!");
        }

        return wallet;
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
    public Wallet updateWallet(Long walletId, Long userId, WalletDto walletDto) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new RuntimeException("Không tìm thấy ví này"));

        if(wallet.getAmount().compareTo(walletDto.getAmount()) < 0) {
            createTransaction(
                    wallet,
                    userId,
                    walletDto.getAmount().subtract(wallet.getAmount()),
                    "income",
                    false,
                    "Khoảng thu do thay đổi số dư ví!"
            );
        } else if (wallet.getAmount().compareTo(walletDto.getAmount()) > 0) {
            createTransaction(wallet, userId, wallet.getAmount().subtract(walletDto.getAmount()), "outcome", false, "Khoảng chi do thay đổi số dư ví!");
        }

        wallet.setWalletName(walletDto.getWalletName());
        wallet.setIcon(walletDto.getIcon());
        wallet.setWalletDescription(walletDto.getWalletDescription());
        wallet.setCurrency(walletDto.getCurrency());
        wallet.setAmount(walletDto.getAmount());
        walletRepository.save(wallet);

        return wallet;
    }

    private void createTransaction(Wallet wallet, Long userId, BigDecimal subtract, String tType, Boolean isTransfer, String note) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng này"));
        Category category = null;
        if(Objects.equals(tType, "income")) {
            category = categoryRepository.findCategoryByCategoryTypeAndIsDefaultAndUser(1, true, user);
        }else {
            category = categoryRepository.findCategoryByCategoryTypeAndIsDefaultAndUser(0, true, user);
        }

        Transaction transaction = Transaction.builder()
                .amount(subtract)
                .wallet(wallet)
                .category(category)
                .datetime(LocalDate.now())
                .note(note)
                .user(user)
                .isTransfer(isTransfer)
                .build();
        transactionRepository.save(transaction);
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
        try {
            Wallet wallet = walletRepository.findById(walletId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy ví"));

            User user = userRepository.findUserByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

            boolean alreadyShared = wallet.getWalletRoles().stream()
                    .anyMatch(role -> role.getUser().getId().equals(user.getId()));

            if (alreadyShared) {
                throw new RuntimeException("Ví này đã được chia sẻ với người dùng này.");
            }

            WalletUserRole walletUserRole = new WalletUserRole();
            walletUserRole.setUser(user);
            walletUserRole.setWallet(wallet);
            walletUserRole.setRole(WalletRole.valueOf(walletRoleName));

            wallet.getWalletRoles().add(walletUserRole);
            walletRepository.save(wallet);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void removeWalletShare(Long walletId, String email) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ví"));

        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        WalletUserRole walletUserRole = wallet.getWalletRoles().stream()
                .filter(role -> role.getUser().getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Người dùng không có quyền chia sẻ ví"));

        wallet.getWalletRoles().remove(walletUserRole);
        walletRepository.save(wallet);
    }

    @Override
    public void updateWalletRole(Long walletId, Long userId, WalletRole walletRoleName) {
       WalletUserRole walletUserRole = walletUserRolesRepo.findWalletUserRoleByWalletIdAndUserId(walletId,userId);
       if(walletUserRole == null){
           throw new RuntimeException("not found");

       }
       walletUserRole.setRole(walletRoleName);
       walletUserRolesRepo.save(walletUserRole);

    }

    @Override
    public void transferMoney(Long fromWalletId, Long toWalletId, BigDecimal amount, Long userId) {
        Wallet fromWallet = walletRepository.findById(fromWalletId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ví: " + fromWalletId));
        Wallet toWallet = walletRepository.findById(toWalletId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy ví: " + toWalletId));

        if(!fromWallet.getCurrency().equals(toWallet.getCurrency())) {
            throw new RuntimeException("Không thể chuyển tiền giữa ví khác loại tiền tệ, việc tự động chuyển đổi sẽ gây sai lệch!");
        }

        if (fromWallet.getAmount().compareTo(amount) < 0) {
            throw new RuntimeException("Số dư trong ví không đủ.");
        }
        createTransaction(fromWallet, userId, amount, "outcome", true, "Chuyển tiền đi" );
        createTransaction(toWallet, userId, amount, "income", true, "Nhận tiền đến");

        fromWallet.setAmount(fromWallet.getAmount().subtract(amount));
        toWallet.setAmount(toWallet.getAmount().add(amount));

        walletRepository.save(fromWallet);
        walletRepository.save(toWallet);
    }

    @Override
    public BigDecimal getTotalAmount(Long userId) {
        Optional<BigDecimal> totalAmount = walletRepository.getTotalAmountByUserId(userId);
        if (totalAmount.isEmpty()){
            return BigDecimal.ZERO;
        }
        return totalAmount.get();
    }
}

