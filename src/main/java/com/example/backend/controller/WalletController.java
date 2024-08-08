package com.example.backend.controller;

import com.example.backend.dto.UserDto;
import com.example.backend.dto.WalletDto;
import com.example.backend.dto.WalletInfoDto;
import com.example.backend.dto.response.ResponseSuccess;
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



    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        // Implement logic to delete wallet with given ID
        walletService.deleteWalletByID(id);
        ResponseSuccess response = new ResponseSuccess();
        response.setMessage("Delete wallet");
        response.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(response.getMessage(), response.getStatus());
    }

    @PostMapping("")
    public ResponseEntity<?> addWallet(@Valid @RequestBody WalletDto walletDto, BindingResult result, Authentication authentication) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        ResponseSuccess responseSuccess = walletService.saveWallet(walletDto, (CustomUserDetails) authentication.getPrincipal());
        return new ResponseEntity<>(responseSuccess.getMessage(), responseSuccess.getStatus());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWalletById(@PathVariable Long id) {
        return new ResponseEntity<>(walletService.findWalletById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateWallet(@PathVariable Long id, @Valid @ModelAttribute WalletDto walletDto, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    public ResponseEntity<?> deleteWallet(
            @PathVariable Long id,
            Authentication authentication
    ) {
        UserDto userDto = getUserDto(authentication);
        if (!walletService.isOwner(id, userDto.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        walletService.deleteWallet(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    private UserDto getUserDto(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userService.findUserByEmail(userDetails.getUsername());
    }
}
