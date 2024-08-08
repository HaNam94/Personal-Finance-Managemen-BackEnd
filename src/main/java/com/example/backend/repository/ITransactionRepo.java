package com.example.backend.repository;

import com.example.backend.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITransactionRepo extends JpaRepository<Transaction, Long> {
    @Query(value = "select tr_w.transaction_id from wallet w join  transaction tr on tr_w.wallets_id = :walletId",nativeQuery = true)
    List<Transaction> getTransactionsByWalletId(Long walletId);
    @Query(value = "delete from transaction where id = :id", nativeQuery = true)
    void deleteTransactionById(Long id);
}
