package com.example.backend.service.impl;

import com.example.backend.dto.BudgetDto;
import com.example.backend.dto.BudgetStatisticsDto;
import com.example.backend.dto.TransactionInfoDto;
import com.example.backend.model.entity.Budget;
import com.example.backend.model.entity.Category;
import com.example.backend.model.entity.Transaction;
import com.example.backend.model.entity.User;
import com.example.backend.repository.IBudgetRepo;
import com.example.backend.repository.ICategoryRepo;
import com.example.backend.repository.ITransactionRepo;
import com.example.backend.repository.IUserRepo;
import com.example.backend.security.principals.CustomUserDetails;
import com.example.backend.service.IBudgetService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements IBudgetService {
    private final IBudgetRepo budgetRepository;
    private final ICategoryRepo categoryRepository;
    private final IUserRepo userRepository;
    private  final ITransactionRepo transactionRepository;

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

         Category categories = categoryRepository.findById(budgetDto.getCategoryId()).orElseThrow(()->new NoSuchFieldException("No category"));
         budget.setCategory(categories);

         User user = userRepository.findUserByEmail(customUserDetails.getEmail()).orElseThrow(() -> new NoSuchFieldException("User not found"));
         budget.setUser(user);

         if(budgetDto.getBudgetAmount().compareTo(budget.getBudgetAmount()) > 0){
             throw new RuntimeException ("Số tiền chi tiêu vượt quá ngân sách đã đặt!");
         }

         budgetRepository.save(budget);
         return budget;

    }

    @Override
    public Budget updateBudget(Long id, BudgetDto budgetDto) throws NoSuchFieldException{

        Budget existingBudget = budgetRepository.findById(id)
                .orElseThrow(() -> new NoSuchFieldException("Budget not found"));

        existingBudget.setBudgetName(budgetDto.getBudgetName());
        existingBudget.setBudgetAmount(budgetDto.getBudgetAmount());
        existingBudget.setBudgetDescription(budgetDto.getBudgetDescription());
        existingBudget.setBudgetDate(LocalDate.now());

        Category categories = categoryRepository.findById(budgetDto.getCategoryId()).orElseThrow(()->new NoSuchFieldException("No category"));
        existingBudget.setCategory(categories);

        return budgetRepository.save(existingBudget);
    }

    @SneakyThrows
    @Override
    public BudgetStatisticsDto getBudgetStatistics( Long budgetId,int month, int year) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new NoSuchFieldException("Budget not found"));

        // Lấy danh sách giao dịch trong tháng
        List<TransactionInfoDto> transactions = transactionRepository.findTransactionsByBudgetAndMonth(0, month, year);

        // Tính tổng số tiền đã chi tiêu
        BigDecimal totalSpent = transactions.stream()
                .map(TransactionInfoDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Tính số tiền còn lại trong ngân sách
        BigDecimal remainingAmount = budget.getBudgetAmount().subtract(totalSpent);

        return BudgetStatisticsDto.builder()
                .remainingAmount(remainingAmount)
                .transactions(transactions)
                .build();
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


}
