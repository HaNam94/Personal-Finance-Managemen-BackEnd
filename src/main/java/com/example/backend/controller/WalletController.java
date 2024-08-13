package com.example.backend.controller;

import com.example.backend.dto.UserDto;
import com.example.backend.dto.WalletDto;
import com.example.backend.dto.WalletInfoDto;
import com.example.backend.dto.response.ResponseSuccess;
import com.example.backend.model.WalletRole;
import com.example.backend.model.entity.Wallet;
import com.example.backend.security.principals.CustomUserDetails;
import com.example.backend.service.IUserService;
import com.example.backend.service.IWalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final IWalletService walletService;
    private final IUserService userService;


    @GetMapping("")
    public ResponseEntity<?> getAllWallets(
            Authentication authentication
    ) {
        UserDto userDto = getUserDto(authentication);
        return new ResponseEntity<>(walletService.findAllWalletByUserId(userDto.getId()), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> createWallet(
            Authentication authentication,
            @Validated @RequestBody WalletDto walletDto
    ) {
        UserDto userDto = getUserDto(authentication);
        walletService.saveWallet(userDto.getId(), walletDto);
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWallet(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UserDto userDto = getUserDto(authentication);
        WalletInfoDto walletDto = walletService.getWalletWithPermission(id, userDto.getId());
        if (walletDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(walletDto, HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWallet(
            @PathVariable Long id,
            @Validated @RequestBody WalletDto walletDto,
            Authentication authentication
    ) {
        UserDto userDto = getUserDto(authentication);
        if (!walletService.isOwner(id, userDto.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        walletService.updateWallet(id, walletDto);
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    @PutMapping("/{id}/{newAmount}")
    public ResponseEntity<?> updateWalletAmount(@PathVariable Long id, @PathVariable BigDecimal newAmount ) {
        walletService.updateWalletAmount(id,newAmount);
        return ResponseEntity.ok().build();
    }



    @DeleteMapping("/{walletId}")
    public ResponseEntity<?> deleteWallet(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UserDto userDto = getUserDto(authentication);
        if (!walletService.isOwner(id, userDto.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        walletService.deleteWalletById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/share-wallet/{id}")
    public ResponseEntity<?> shareWallet(@PathVariable("id") Long walletId,
                                         Authentication authentication,
                                         @RequestParam("email") String email,
                                         @RequestParam("role") String walletRoleName) {
        try {
            System.out.println("walletId: " + walletId);
            System.out.println("email: " + email);
            System.out.println("role: " + walletRoleName);
            walletService.shareWallet(walletId, email, walletRoleName);
            return new ResponseEntity<>("{}", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Lỗi xảy ra: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    private UserDto getUserDto(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userService.findUserByEmail(userDetails.getUsername());
    }
    @DeleteMapping("/share-wallet/{walletId}")
    public ResponseEntity<?> removeWalletShare(
            @PathVariable Long walletId,
            @RequestParam String email) {

        try {
            walletService.removeWalletShare(walletId, email);
            return ResponseEntity.ok().body("Tài khoản đã được xóa khỏi danh sách chia sẻ.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PutMapping("/share-wallet/{id}")
    public ResponseEntity<?> updateWalletRole(@PathVariable("id") Long walletId,
                                              @RequestParam("userId") Long userId,
                                              @RequestParam("role") WalletRole walletRoleName) {
        try {
            System.out.println("walletId: " + walletId);
            System.out.println("userId: " + userId);
            System.out.println("role: " + walletRoleName);
            walletService.updateWalletRole(walletId, userId, walletRoleName);
            return new ResponseEntity<>("Cập nhật quyền thành công", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Lỗi xảy ra: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
