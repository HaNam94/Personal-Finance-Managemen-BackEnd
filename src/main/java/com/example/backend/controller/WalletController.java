package com.example.backend.controller;

import com.example.backend.dto.WalletDto;
import com.example.backend.dto.response.ResponseSuccess;
import com.example.backend.model.entity.Wallet;
import com.example.backend.security.principals.CustomUserDetails;
import com.example.backend.service.IWalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/vi/public/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final IWalletService walletService;

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        // Implement logic to delete wallet with given ID
        walletService.deleteWalletByID(id);
        ResponseSuccess response = new ResponseSuccess();
        response.setMessage("Delete wallet");
        response.setStatus(HttpStatus.OK);
        return new ResponseEntity<>(response.getMessage(), response.getStatus());
    }

    @PostMapping
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

    @PostMapping("/{id}")
    public ResponseEntity<?> updateWallet(@PathVariable Long id, @Valid @RequestBody WalletDto walletDto, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        ResponseSuccess responseSuccess = walletService.updateWallet(id, walletDto);
        return new ResponseEntity<>(responseSuccess.getMessage(), responseSuccess.getStatus());
    }
}
