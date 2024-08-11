package com.example.backend.dto;

public interface SubCategoryDto {
    Long getId();
    String getCategoryName();
    String getNote();
    String getIcon();
    Integer getCategoryType();
    Boolean getIsDefault();
}