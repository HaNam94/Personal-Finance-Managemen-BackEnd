package com.example.backend.dto;

import com.example.backend.model.entity.Category;
import org.springframework.beans.factory.annotation.Value;

import java.util.Set;

public interface CategoryDto {
    Long getId();
    String getCategoryName();
    String getNote();
    String getIcon();
    Integer getCategoryType();
    Boolean getIsDefault();
    Set<SubCategoryDto> getSubCategories();
    @Value("#{target.user.id}")
    Long getUserId();
}


