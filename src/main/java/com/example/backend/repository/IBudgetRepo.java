package com.example.backend.repository;

import com.example.backend.model.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface IBudgetRepo extends JpaRepository<Budget, Long> {

    @Query(value = "select b.id from user u join budget b on :userId = b.user_id", nativeQuery = true)
    List<Budget> getBudgetsByUserId(Long userId);
    @Query(value = "call delete_budget(:id)", nativeQuery = true)
    void deleteBudgetById(Long id);
}
