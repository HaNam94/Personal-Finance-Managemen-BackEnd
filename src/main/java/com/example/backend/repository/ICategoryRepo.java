package com.example.backend.repository;

import com.example.backend.dto.CategoryDto;
import com.example.backend.model.entity.Category;
import com.example.backend.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICategoryRepo extends JpaRepository<Category, Long> {
    Iterable<CategoryDto> findAllByParentCategoryIsNullAndUserId(Long id);
    Optional<CategoryDto> findCategoryById(Long id);
    Category findCategoryByCategoryTypeAndIsDefaultAndUser(Integer categoryType, Boolean isDefault, User user);
}
