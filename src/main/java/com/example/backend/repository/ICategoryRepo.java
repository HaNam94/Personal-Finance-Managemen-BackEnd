package com.example.backend.repository;

import com.example.backend.dto.CategoryDto;
import com.example.backend.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepo extends JpaRepository<Category, Long> {
    Iterable<CategoryDto> findAllByParentCategoryIsNullAndUserId(Long id);
}
