package com.example.backend.service.impl;

import com.example.backend.dto.CategoryDto;
import com.example.backend.model.entity.Category;
import com.example.backend.repository.ICategoryRepo;
import com.example.backend.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private ICategoryRepo categoryRepo;
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
}
