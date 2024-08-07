package com.example.backend.service.impl;

import com.example.backend.dto.BudgetDto;
import com.example.backend.model.entity.Budget;
import com.example.backend.model.entity.Category;
import com.example.backend.model.entity.User;
import com.example.backend.repository.IBudgetRepo;
import com.example.backend.repository.ICategoryRepo;
import com.example.backend.repository.IUserRepo;
import com.example.backend.security.principals.CustomUserDetails;
import com.example.backend.service.IBudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements IBudgetService {
    private final IBudgetRepo budgetRepository;
    private final ICategoryRepo categoryRepository;
    private final IUserRepo userRepository;

    @Override
    public List<Budget> findAllBudgets() {
        return budgetRepository.findAll();
    }

    @Override
    public Budget save(BudgetDto budgetDto, CustomUserDetails customUserDetails) throws NoSuchFieldException {
        Budget budget = Budget.builder()
                .budgetName(budgetDto.getBudgetName())
                .budgetType(budgetDto.getBudgetType())
                .budgetAmount(budgetDto.getBudgetAmount())
                .budgetDescription(budgetDto.getBudgetDescription())
                .budgetDate(LocalDate.now())
                .build();

        List<Category> categories = categoryRepository.findAll();
        budget.setCategory(categories);
        User user = userRepository.findUserByEmail(customUserDetails.getEmail()).orElseThrow(() -> new NoSuchFieldException("User not found"));
        budget.setUser(user);
        budgetRepository.save(budget);
        return null;
    }

    @Override
    public Budget findBudgetById(Long id) {
        return null;
    }

    @Override
    public void deleteBudgetById(Long id) {

    }
}
