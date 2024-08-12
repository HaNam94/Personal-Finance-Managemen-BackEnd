package com.example.backend.dto;

import com.example.backend.model.entity.Category;

import java.util.Set;

public interface CategoryDto {
    Long getId();
    String getCategoryName();
    String getNote();
    String getIcon();
    Integer getCategoryType();
    Boolean getIsDefault();
    Set<SubCategoryDto> getSubCategories();
}


