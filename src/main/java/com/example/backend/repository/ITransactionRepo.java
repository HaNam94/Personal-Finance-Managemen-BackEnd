package com.example.backend.repository;

import com.example.backend.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITransactionRepo extends JpaRepository<Transaction, Long> {
}
