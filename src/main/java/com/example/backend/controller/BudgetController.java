package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.model.entity.Budget;
import com.example.backend.security.principals.CustomUserDetails;
import com.example.backend.service.IBudgetService;
import com.example.backend.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/budgets")
@CrossOrigin("*")
@RequiredArgsConstructor
public class BudgetController {
    private final IBudgetService budgetService;
    private final IUserService userService;

    @GetMapping
    public ResponseEntity<?> getAll(
            Authentication authentication
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserDto userDto = userService.findUserByEmail(userDetails.getUsername());
        Iterable<BudgetInfoDto> budgetInfoDtos = budgetService.findAll(userDto.getId());
        return new ResponseEntity<>(budgetInfoDtos, HttpStatus.OK);
    }

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

    @GetMapping("/{id}/statistics")
    public ResponseEntity<BudgetStatisticsDto> getBudgetStatistics(@PathVariable Long id,
                                                                   @RequestParam int month,
                                                                   @RequestParam int year) {
        BudgetStatisticsDto statistics = budgetService.getBudgetStatistics(id, month, year);
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }
}

