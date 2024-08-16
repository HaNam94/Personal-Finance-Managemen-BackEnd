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

                .budgetAmount(budgetDto.getBudgetAmount())
                .budgetDescription(budgetDto.getBudgetDescription())
                .budgetDate(LocalDate.now())
                .build();

//        List<Category> categories = categoryRepository.findAll();
//        budget.setCategory(categories);

        User user = userRepository.findUserByEmail(customUserDetails.getEmail()).orElseThrow(() -> new NoSuchFieldException("User not found"));
        budget.setUser(user);

        if(budgetDto.getBudgetAmount().compareTo(budget.getBudgetAmount()) > 0){
            throw new RuntimeException ("Số tiền chi tiêu vượt quá ngân sách đã đặt!");
        }

        budgetRepository.save(budget);
        return null;
    }

    @Override
    public Budget updateBudget(Long id, BudgetDto budgetDto) throws NoSuchFieldException{
        Budget existingBudget = budgetRepository.findById(id)
                .orElseThrow(() -> new NoSuchFieldException("Budget not found"));

        existingBudget.setBudgetName(budgetDto.getBudgetName());
        existingBudget.setBudgetAmount(budgetDto.getBudgetAmount());
        existingBudget.setBudgetDescription(budgetDto.getBudgetDescription());
        existingBudget.setBudgetDate(LocalDate.now());

        return budgetRepository.save(existingBudget);
    }

    @Override
    public Budget findBudgetById(Long id) {
        return null;
    }


    @Override
    public void deleteBudgetById(Long id) throws NoSuchFieldException {
        Budget existingBudget = budgetRepository.findById(id)
                .orElseThrow(() -> new NoSuchFieldException("Budget not found"));

        budgetRepository.delete(existingBudget);
    }

    @Override
    public List<Budget> getMonthlyBudgetStatistics(Long userId, int year, int month){
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return budgetRepository.findBudgetsByUserIdAndDateRange(userId, startDate, endDate);
    }
}
