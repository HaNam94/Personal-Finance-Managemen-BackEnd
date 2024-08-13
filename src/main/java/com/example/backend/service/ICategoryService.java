package com.example.backend.service;

import com.example.backend.dto.CategoryDto;
import com.example.backend.dto.CategoryFormDto;

public interface ICategoryService {
    Iterable<CategoryDto> findAllMasterCategoryByUserId(Long id);

    void deleteCategory(Long id, Long id1);

    void saveCategory(CategoryFormDto categoryFormDto, Long id);

    CategoryDto findCategoryById(Long id);

    void updateCategory(Long id, CategoryFormDto categoryFormDto);
}
