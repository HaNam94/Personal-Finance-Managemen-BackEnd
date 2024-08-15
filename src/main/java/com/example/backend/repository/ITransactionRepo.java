package com.example.backend.repository;

import com.example.backend.dto.TransactionInfoDto;
import com.example.backend.dto.TransactionSimpleDto;
import com.example.backend.model.entity.Transaction;
import jakarta.persistence.JoinColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITransactionRepo extends JpaRepository<Transaction, Long> {


    @Query(value = "delete from transaction where user_id = :userId", nativeQuery = true)
    void deleteTransactionByUserId(Long userId);

   @Query("SELECT t.id as id, " +
           "t.note as note, " +
           "t.amount as amount, " +
           "t.datetime as datetime, " +
           "c.icon as icon, " +
           "c.categoryType as categoryType, " +
           "c.categoryName as categoryName, " +
           "c.id as categoryId, " +
           "w.walletName as walletName," +
           "w.id as walletId  " +
           "FROM Transaction t JOIN  t.category c JOIN  t.wallet w  " +
           "WHERE t.user.id = :userId" )
    List<TransactionInfoDto> findAllTransactionByUserId(Long userId);

    List<TransactionSimpleDto> findAllByUserIdAndCategoryId(Long categoryId, Long userId);


}
