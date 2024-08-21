package com.example.backend.repository;

import com.example.backend.dto.BudgetInfoDto;
import com.example.backend.model.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository

public interface IBudgetRepo extends JpaRepository<Budget, Long> {

    @Query(value = "select b.id from user u join budget b on :userId = b.user_id", nativeQuery = true)
    List<Budget> getBudgetsByUserId(Long userId);
    @Query(value = "call delete_budget(:id)", nativeQuery = true)
    void deleteBudgetById(Long id);
    @Query("SELECT b FROM Budget b WHERE b.user.id = :userId AND b.budgetDate BETWEEN :startDate AND :endDate")
    List<Budget> findBudgetsByUserIdAndDateRange(@Param("userId") Long userId,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);

    @Query("SELECT b FROM Budget b WHERE b.user.id = :userId")
    Iterable<BudgetInfoDto> findAllByUserId(Long userId);

    Boolean existsBudgetByUserIdAndCategoryId(Long userId, Long categoryId);
    Budget findByUserIdAndCategoryId(Long userId, Long categoryId);
}
