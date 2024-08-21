package com.example.backend.service.impl;

import com.example.backend.dto.CategoryDto;
import com.example.backend.dto.CategoryFormDto;
import com.example.backend.model.entity.Budget;
import com.example.backend.model.entity.Category;
import com.example.backend.model.entity.Transaction;
import com.example.backend.model.entity.User;
import com.example.backend.repository.IBudgetRepo;
import com.example.backend.repository.ICategoryRepo;
import com.example.backend.repository.ITransactionRepo;
import com.example.backend.repository.IUserRepo;
import com.example.backend.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private ICategoryRepo categoryRepo;
    @Autowired
    private IUserRepo userRepo;
    @Autowired
    private ITransactionRepo transactionRepository;
    @Autowired
    private IBudgetRepo budgetRepository;


    @Override
    public Iterable<CategoryDto> findAllMasterCategoryByUserId(Long id) {
        return categoryRepo.findAllByParentCategoryIsNullAndUserId(id);
    }
    @Override
    public void deleteCategory(Long id, Long userId) {
        Category category = categoryRepo.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));

        // Ensure the user owns the category
        if (!category.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        // Update transactions for subcategories first
        for (Category subCategory : category.getSubCategories()) {
            setDefaultCategory(subCategory);
            // Delete budgets associated with the subcategory
            Budget budget = budgetRepository.findByUserIdAndCategoryId(userId, subCategory.getId());
            if (budget != null) {
                budgetRepository.deleteById(budget.getId());
            }
            categoryRepo.delete(subCategory);
        }

        // Update transactions for the main category
        setDefaultCategory(category);

        // Delete budgets associated with the category
        Budget budget = budgetRepository.findByUserIdAndCategoryId(userId, category.getId());
        if (budget != null) {
            budgetRepository.deleteById(budget.getId());
        }

        categoryRepo.delete(category);
    }

    public void setDefaultCategory(Category category) {
        Category defaultCategory;
        if (category.getCategoryType() == 1) {
            defaultCategory = categoryRepo.findCategoryByCategoryTypeAndIsDefaultAndUser(1, true, category.getUser());
        } else {
            defaultCategory = categoryRepo.findCategoryByCategoryTypeAndIsDefaultAndUser(0, true, category.getUser());
        }

        // Ensure that there is a default category set
        if (defaultCategory == null) {
            throw new RuntimeException("Default category not found for user: " + category.getUser().getId());
        }

        for (Transaction transaction : category.getTransactions()) {
            transaction.setCategory(defaultCategory);
            transactionRepository.save(transaction);  // Save the transaction with the updated category
        }
    }

    @Override
    public void saveCategory(CategoryFormDto categoryFormDto, Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Category category = new Category();
        category.setCategoryName(categoryFormDto.getCategoryName());
        category.setUser(user);
        category.setCategoryType(categoryFormDto.getCategoryType());
        category.setNote(categoryFormDto.getNote());
        category.setIcon(categoryFormDto.getIcon());

        if(categoryFormDto.getParentId() != null) {
            Category parentCategory = categoryRepo.findById(categoryFormDto.getParentId()).orElseThrow(() -> new RuntimeException("Cannot find parent category"));
            category.setParentCategory(parentCategory);
            category.setCategoryType(parentCategory.getCategoryType());
        }
        categoryRepo.save(category);
    }

    @Override
    public CategoryDto findCategoryById(Long id) {
        return categoryRepo.findCategoryById(id).orElse(null);
    }

    @Override
    public void updateCategory(Long id, CategoryFormDto categoryFormDto) {
        Category category = categoryRepo.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        category.setCategoryName(categoryFormDto.getCategoryName());
        category.setNote(categoryFormDto.getNote());
        category.setIcon(categoryFormDto.getIcon());
        categoryRepo.save(category);
    }
}
