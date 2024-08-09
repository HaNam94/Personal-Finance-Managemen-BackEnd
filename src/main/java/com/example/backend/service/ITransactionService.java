package com.example.backend.service;

import com.example.backend.dto.TransactionDto;
import com.example.backend.model.entity.Transaction;

import java.util.List;

public interface ITransactionService {
    List<Transaction> findAllTransactionByUserId(Long id);

    Transaction findTransactionById(Long id);

    void save(Long userId,TransactionDto transactionDto);

    void deleteById(Long id);

    void updateTransaction(Long id, TransactionDto transactionDto);

}
