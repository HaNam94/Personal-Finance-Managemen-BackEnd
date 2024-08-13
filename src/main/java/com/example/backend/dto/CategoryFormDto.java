package com.example.backend.dto;

import com.example.backend.model.entity.Category;
import com.example.backend.model.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CategoryFormDto {
    private Long id;
    private String categoryName;
    private String icon;
    private String note;
    private Integer categoryType;
    private Long parentId;
}
