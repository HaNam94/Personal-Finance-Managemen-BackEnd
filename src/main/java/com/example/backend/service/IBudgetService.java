package com.example.backend.service;

import com.example.backend.dto.BudgetDto;
import com.example.backend.dto.BudgetStatisticsDto;
import com.example.backend.model.entity.Budget;
import com.example.backend.security.principals.CustomUserDetails;

import java.util.List;

public interface IBudgetService {
    List<Budget> findAllBudgets();
    Budget save(BudgetDto budgetDto, CustomUserDetails customUserDetails) throws NoSuchFieldException;
    Budget findBudgetById(Long id);
    void deleteBudgetById(Long id) throws NoSuchFieldException;
    Budget updateBudget(Long id, BudgetDto budgetDto) throws NoSuchFieldException;

    BudgetStatisticsDto getBudgetStatistics(Long id, int month, int year);
}
