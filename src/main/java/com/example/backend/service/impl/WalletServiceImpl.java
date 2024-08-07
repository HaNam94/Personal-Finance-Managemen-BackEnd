package com.example.backend.service.impl;

import com.example.backend.dto.WalletDto;
import com.example.backend.dto.response.ResponseSuccess;
import com.example.backend.model.RoleName;
import com.example.backend.model.entity.Role;
import com.example.backend.model.entity.User;
import com.example.backend.model.entity.Wallet;
import com.example.backend.repository.IRoleRepo;
import com.example.backend.repository.IUserRepo;
import com.example.backend.repository.IWalletRepo;
import com.example.backend.security.principals.CustomUserDetails;
import com.example.backend.security.principals.CustomUserDetailsService;
import com.example.backend.service.IWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements IWalletService {
    private final IWalletRepo walletRepository;
    private final IUserRepo userRepository;
    private final IRoleRepo roleRepository;


    @Override
    public ResponseSuccess saveWallet(WalletDto walletDto, CustomUserDetails customUserDetails) {
        User user = userRepository.findUserByEmail(customUserDetails.getEmail()).orElseThrow(()->new RuntimeException("User not found"));
        Wallet wallet = Wallet.builder()
                .walletName(walletDto.getWalletName())
                .icon(walletDto.getIcon())
                .walletDescription(walletDto.getWalletDescription())
                .currency(walletDto.getCurrency())
                .amount(walletDto.getAmount())
                .walletStatus(true)
                .build();
        Set<User> users = new HashSet<>();
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findRolesByRoleName(String.valueOf(RoleName.ROLE_USER));
        users.add(user);
        roles.add(role);
        wallet.setUsers(users);
        wallet.setRoles(roles);
        walletRepository.save(wallet);
        ResponseSuccess responseSuccess = new ResponseSuccess();
        responseSuccess.setMessage("Successfully saved wallet");
        responseSuccess.setStatus(HttpStatus.CREATED);
        return responseSuccess;
    }

    @Override
    public ResponseSuccess updateWallet(Long id, WalletDto walletDto) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(()->new RuntimeException("Wallet not found"));
        wallet.setWalletName(walletDto.getWalletName());
        wallet.setCurrency(walletDto.getCurrency());
        wallet.setAmount(walletDto.getAmount());
        wallet.setIcon(walletDto.getIcon());
        wallet.setWalletDescription(walletDto.getWalletDescription());
        walletRepository.save(wallet);
        ResponseSuccess responseSuccess = new ResponseSuccess();
        responseSuccess.setMessage("Successfully updated wallet");
        responseSuccess.setStatus(HttpStatus.OK);

        return responseSuccess;
    }

    @Override
    public WalletDto findWalletById(Long id) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(()->new RuntimeException("Wallet not found"));
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
}
