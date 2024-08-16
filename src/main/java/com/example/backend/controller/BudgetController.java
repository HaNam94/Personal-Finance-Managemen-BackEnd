package com.example.backend.controller;

import com.example.backend.dto.BudgetDto;
import com.example.backend.model.entity.Budget;
import com.example.backend.security.principals.CustomUserDetails;
import com.example.backend.service.IBudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {
    private final IBudgetService budgetService;

    @PostMapping
    public ResponseEntity<Budget> createBudget(@RequestBody BudgetDto budgetDto,
                                               @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        try {
            Budget newBudget = budgetService.save(budgetDto, customUserDetails);
            return new ResponseEntity<>(newBudget, HttpStatus.CREATED);
        } catch (NoSuchFieldException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Budget> updateBudget(@PathVariable Long id,
                                               @RequestBody BudgetDto budgetDto) {
        try {
            Budget updatedBudget = budgetService.updateBudget(id, budgetDto);
            return new ResponseEntity<>(updatedBudget, HttpStatus.OK);
        } catch (NoSuchFieldException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        try {
            budgetService.deleteBudgetById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoSuchFieldException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

 /*   @GetMapping("/statistics/{year}/{month}")
    public ResponseEntity<List<Budget>> getMonthlyBudgetStatistics(@PathVariable int year,
                                                                   @PathVariable int month,
                                                                   @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        List<Budget> monthlyBudgets = budgetService.getMonthlyBudgetStatistics(CustomUserDetails.getUserId(), year, month);
        return new ResponseEntity<>(monthlyBudgets, HttpStatus.OK);
    } */
}

