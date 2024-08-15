package com.example.backend.service;

import com.example.backend.dto.TransactionDto;
import com.example.backend.dto.TransactionInfoDto;
import com.example.backend.dto.TransactionSimpleDto;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ITransactionService {

    TransactionDto findTransactionById(Long id);

    TransactionDto save(Long userId,TransactionDto transactionDto);

    void deleteById(Long id);

    void updateTransaction(Long id, TransactionDto transactionDto);

    List<TransactionSimpleDto> searchTransactionWithUserId(Long id, Long categoryId);

    Page<TransactionInfoDto> findAllTransactionByUserId(Long id, Long categoryId, Integer categoryType, int page);

    BigDecimal statisticalTotalAmountTodayByCategoryType(Long userId, Integer categoryType);

    BigDecimal statisticalTotalAmountByTypeAndTime(Integer type, LocalDate fromDate, LocalDate toDate, Long id);

    BigDecimal statisticalAmountTodayByWalletId(Integer categoryType, Long walletId);

    BigDecimal statisticalAmountByWalletIdAndTime(Integer categoryType, Long walletId, LocalDate fromDate, LocalDate toDate);
}
