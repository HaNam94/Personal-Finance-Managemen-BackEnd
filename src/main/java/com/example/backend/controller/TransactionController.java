package com.example.backend.controller;

import com.example.backend.dto.TransactionDto;
import com.example.backend.service.ITransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/user/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private ITransactionService transactionService;

    @PostMapping("/add")
    public ResponseEntity<?> addTransaction(@Valid @RequestBody TransactionDto transactionDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
           for (FieldError error : bindingResult.getFieldErrors()) {
               errors.put(error.getField(), error.getDefaultMessage());
           }
            return ResponseEntity.badRequest().body(errors);
        }
        transactionService.save(transactionDto);
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
