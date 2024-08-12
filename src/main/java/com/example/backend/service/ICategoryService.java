package com.example.backend.service;

import com.example.backend.dto.CategoryDto;

public interface ICategoryService {
    Iterable<CategoryDto> findAllMasterCategoryByUserId(Long id);

    void deleteCategory(Long id, Long id1);
}
