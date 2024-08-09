package com.example.backend.service.impl;

import com.example.backend.dto.TransactionDto;
import com.example.backend.model.entity.Category;
import com.example.backend.model.entity.Transaction;
import com.example.backend.model.entity.Wallet;
import com.example.backend.repository.ICategoryRepo;
import com.example.backend.repository.ITransactionRepo;
import com.example.backend.repository.IUserRepo;
import com.example.backend.repository.IWalletRepo;
import com.example.backend.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl  implements ITransactionService {
private final ITransactionRepo transactionRepository;
    private final ICategoryRepo categoryRepository;
    private final IWalletRepo walletRepository;
    private final IUserRepo userRepository;


    @Override
    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public void updateTransaction(Long id, TransactionDto transactionDto) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));
        Category category = categoryRepository.findById(transactionDto.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
        Wallet wallet = walletRepository.findById(transactionDto.getWalletId()).orElseThrow(() -> new RuntimeException("Wallet not found"));

        transaction.setTransactionType(category.getCategoryType());
        transaction.setNote(transactionDto.getNote());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setDatetime(transactionDto.getDatetime());
        transaction.setCategory(category);
        transaction.setWallet(wallet);
        if (transaction.getTransactionType() == 1) {
            wallet.setAmount(wallet.getAmount().add(transactionDto.getAmount()));
        }else if (transaction.getTransactionType() == 0) {
            wallet.setAmount(wallet.getAmount().subtract(transactionDto.getAmount()));
        }
        walletRepository.save(wallet);
        transactionRepository.save(transaction);

    }

}
