package com.example.backend.service;

import com.example.backend.dto.TransactionDto;
import com.example.backend.dto.TransactionInfoDto;
import com.example.backend.dto.TransactionSimpleDto;
import java.util.List;

public interface ITransactionService {
    List<TransactionInfoDto> findAllTransactionByUserId(Long id);

    TransactionDto findTransactionById(Long id);

    TransactionDto save(Long userId,TransactionDto transactionDto);

    void deleteById(Long id);

    void updateTransaction(Long id, TransactionDto transactionDto);

    List<TransactionSimpleDto> searchTransactionWithUserId(Long id, Long categoryId);
}
