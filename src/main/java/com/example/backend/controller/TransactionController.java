package com.example.backend.controller;

import com.example.backend.dto.TransactionDto;
import com.example.backend.dto.TransactionInfoDto;
import com.example.backend.dto.TransactionSimpleDto;
import com.example.backend.dto.UserDto;
import com.example.backend.model.entity.User;
import com.example.backend.security.principals.CustomUserDetails;
import com.example.backend.service.ITransactionService;
import com.example.backend.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
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

    @GetMapping
    public ResponseEntity<?> getAllTransactions(
            Authentication authentication,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer categoryType,
            @RequestParam(defaultValue = "0") int page
    ) {
        UserDto user = getUserDto(authentication);
        Page<TransactionInfoDto> transactions = transactionService.findAllTransactionByUserId(user.getId(), categoryId, categoryType, page);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping
    public ResponseEntity<?> addTransaction(@Valid @RequestBody TransactionDto transactionDto, BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }
        UserDto user = getUserDto(authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.save(user.getId(), transactionDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransaction(@PathVariable Long id, @Valid @RequestBody TransactionDto transactionDto, BindingResult bindingResult) {
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
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchTransactions(
            @RequestParam Long categoryId,
            Authentication authentication
    ) {
        UserDto user = getUserDto(authentication);
        List<TransactionSimpleDto> transactions = transactionService.searchTransactionWithUserId(user.getId(), categoryId);
        return ResponseEntity.ok().body(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable Long id) {
        return new ResponseEntity<>(transactionService.findTransactionById(id), HttpStatus.OK);
    }

    @GetMapping("/all-amount-today/{categoryType}")
    public ResponseEntity<?> getAllMountToday(@PathVariable("categoryType") Integer categoryType, Authentication authentication) {
        UserDto userDto = getUserDto(authentication);
        return new ResponseEntity<>(transactionService.getTotalAmountToday(userDto.getId(), categoryType), HttpStatus.OK);
    }
}
