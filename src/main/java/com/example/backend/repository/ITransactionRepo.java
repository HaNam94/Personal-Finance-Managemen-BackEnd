package com.example.backend.repository;

import com.example.backend.dto.TransactionInfoDto;
import com.example.backend.dto.TransactionSimpleDto;
import com.example.backend.model.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ITransactionRepo extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE " +
            "(t.user.id = :userId) AND " +
            "(:categoryId IS NULL OR t.category.id = :categoryId) AND " +
            "(:categoryType IS NULL OR :categoryType = 2 OR t.category.categoryType = :categoryType) " +
            "ORDER BY t.datetime DESC")
    Page<TransactionInfoDto> findAllTransactionByUserId(@Param("userId") Long userId,
                                                        @Param("categoryId") Long categoryId,
                                                        @Param("categoryType") Integer categoryType,
                                                        Pageable pageable);

    List<TransactionSimpleDto> findAllByUserIdAndCategoryId(Long categoryId, Long userId);

    @Query("SELECT SUM(t.amount) AS totalAmount " +
            "FROM Transaction t " +
            "WHERE t.user.id = :userId and t.category.categoryType = :categoryType AND t.datetime = :datetime")
    Optional<BigDecimal> getTotalAmountTodayByCategoryType(@Param("userId") Long userId,
                                                           @Param("categoryType") Integer categoryType,
                                                           @Param("datetime") LocalDate datetime);

    @Query("SELECT SUM(t.amount) " +
            "FROM Transaction  t " +
            "WHERE t.user.id = :userId AND t.category.categoryType = :type " +
            "AND t.datetime BETWEEN :fromDate AND :toDate")
    Optional<BigDecimal> getTotalAmountByTypeAndTime(@Param("type") Integer type,
                                                     @Param("fromDate") LocalDate fromDate,
                                                     @Param("toDate") LocalDate toDate,
                                                     @Param("userId") Long userId);
    @Query("SELECT SUM(t.amount) " +
            "FROM Transaction t " +
            "WHERE t.category.categoryType = :categoryType " +
            "AND t.wallet.id = :walletId AND t.datetime = :time")
    Optional<BigDecimal> statisticalAmountTodayByWalletId(@Param("categoryType") Integer categoryType,
                                                          @Param("walletId") Long walletId,
                                                          @Param("time") LocalDate time);
    @Query("SELECT SUM(t.amount) " +
            "FROM Transaction t " +
            "WHERE t.category.categoryType = :categoryType " +
            "AND t.wallet.id = :walletId AND t.datetime BETWEEN :fromDate AND :toDate")
    Optional<BigDecimal> statisticalAmountByWalletIdAndTime(@Param("categoryType") Integer categoryType,
                                                            @Param("walletId") Long walletId,
                                                            @Param("fromDate") LocalDate fromDate,
                                                            @Param("toDate") LocalDate toDate);
}
