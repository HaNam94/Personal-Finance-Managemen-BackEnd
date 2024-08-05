package com.example.backend.repository;

import com.example.backend.model.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBudgetRepo extends JpaRepository<Budget, Long> {
}
