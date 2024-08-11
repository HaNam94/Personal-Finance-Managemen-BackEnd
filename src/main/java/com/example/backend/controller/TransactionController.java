package com.example.backend.controller;

import com.example.backend.dto.TransactionDto;
import com.example.backend.dto.TransactionInfoDto;
import com.example.backend.dto.UserDto;
import com.example.backend.model.entity.Transaction;
import com.example.backend.security.principals.CustomUserDetails;
import com.example.backend.service.ITransactionService;
import com.example.backend.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final ITransactionService transactionService;
    private final IUserService userService;


    private UserDto getUserDto(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userService.findUserByEmail(userDetails.getUsername());
    }

    @GetMapping("")
    public  ResponseEntity<?> getAllTransactions(Authentication authentication) {
        UserDto user =  getUserDto(authentication);
       List<Transaction> transactions = transactionService.findAllTransactionByUserId(user.getId());
        return ResponseEntity.ok().body(transactions);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTransaction(@Valid @RequestBody TransactionDto transactionDto, BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
           for (FieldError error : bindingResult.getFieldErrors()) {
               errors.put(error.getField(), error.getDefaultMessage());
           }
            return ResponseEntity.badRequest().body(errors);
        }
        UserDto user =  getUserDto(authentication);
        transactionService.save(user.getId(),transactionDto);
        return ResponseEntity.ok().body(transactionDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransaction(@PathVariable Long id, @Valid @ModelAttribute TransactionDto transactionDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        transactionService.updateTransaction(id, transactionDto);
        return ResponseEntity.ok().body(transactionDto);
    }

    @DeleteMapping("/{id}")
        public ResponseEntity<?> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
