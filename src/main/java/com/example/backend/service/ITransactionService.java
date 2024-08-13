package com.example.backend.service;

import com.example.backend.dto.TransactionDto;
import com.example.backend.dto.TransactionInfoDto;

import java.util.List;

public interface ITransactionService {
    List<TransactionInfoDto> findAllTransactionByUserId(Long id);

    TransactionDto findTransactionById(Long id);

    void save(Long userId,TransactionDto transactionDto);

    void deleteById(Long id);

    void updateTransaction(Long id, TransactionDto transactionDto);

}
