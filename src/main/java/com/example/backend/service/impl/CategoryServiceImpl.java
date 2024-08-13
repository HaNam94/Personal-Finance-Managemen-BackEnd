package com.example.backend.service.impl;

import com.example.backend.dto.CategoryDto;
import com.example.backend.dto.CategoryFormDto;
import com.example.backend.model.entity.Category;
import com.example.backend.model.entity.User;
import com.example.backend.repository.ICategoryRepo;
import com.example.backend.repository.IUserRepo;
import com.example.backend.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private ICategoryRepo categoryRepo;
    @Autowired
    private IUserRepo userRepo;
    @Override
    public Iterable<CategoryDto> findAllMasterCategoryByUserId(Long id) {
        return categoryRepo.findAllByParentCategoryIsNullAndUserId(id);
    }

    @Override
    public void deleteCategory(Long id, Long userId) {
        Category category = categoryRepo.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        if (!category.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access");
        }

        categoryRepo.deleteById(id);
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
