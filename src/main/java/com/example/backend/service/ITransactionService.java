package com.example.backend.service;

import com.example.backend.dto.TransactionDto;

public interface ITransactionService {
    void save(TransactionDto transactionDto);

    void deleteById(Long id);

    void updateTransaction(Long id, TransactionDto transactionDto);
}
