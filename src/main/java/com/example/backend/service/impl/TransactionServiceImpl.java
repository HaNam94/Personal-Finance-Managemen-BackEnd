package com.example.backend.service.impl;

import com.example.backend.dto.TransactionDto;
import com.example.backend.dto.TransactionInfoDto;
import com.example.backend.model.entity.Category;
import com.example.backend.model.entity.Transaction;
import com.example.backend.model.entity.User;
import com.example.backend.model.entity.Wallet;
import com.example.backend.repository.ICategoryRepo;
import com.example.backend.repository.ITransactionRepo;
import com.example.backend.repository.IUserRepo;
import com.example.backend.repository.IWalletRepo;
import com.example.backend.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements ITransactionService {

    private final ITransactionRepo transactionRepository;
    private final ICategoryRepo categoryRepository;
    private final IWalletRepo walletRepository;
    private final IUserRepo userRepository;


    @Override
    public List<TransactionInfoDto> findAllTransactionByUserId(Long userId) {
        return transactionRepository.findAllTransactionByUserId(userId);
    }

    @Override
    public Transaction findTransactionById(Long id) {
        return transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    @Override
    public void save(Long userId,TransactionDto transactionDto) {
        Category category = categoryRepository.findById(transactionDto.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
        Wallet wallet = walletRepository.findById(transactionDto.getWalletId()).orElseThrow(() -> new RuntimeException("Wallet not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Transaction transaction = Transaction.builder()
                .amount(transactionDto.getAmount())
                .note(transactionDto.getNote())
                .datetime(transactionDto.getDatetime())
                .category(category)
                .wallet(wallet)
                .user(user)
                .build();
        if (transaction.getCategory().getCategoryType() == 1) {
            wallet.setAmount(wallet.getAmount().add(transactionDto.getAmount()));
        }else if (transaction.getCategory().getCategoryType() == 0) {
            wallet.setAmount(wallet.getAmount().subtract(transactionDto.getAmount()));
        }
        walletRepository.save(wallet);
        transactionRepository.save(transaction);
    }

    @Override
    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public void updateTransaction(Long id, TransactionDto transactionDto) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));
        Category category = categoryRepository.findById(transactionDto.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
        Wallet wallet = walletRepository.findById(transactionDto.getWalletId()).orElseThrow(() -> new RuntimeException("Wallet not found"));


        transaction.setNote(transactionDto.getNote());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setDatetime(transactionDto.getDatetime());
        transaction.setCategory(category);
        transaction.setWallet(wallet);
        if (transaction.getCategory().getCategoryType() == 1) {
            wallet.setAmount(wallet.getAmount().add(transactionDto.getAmount()));
        }else if (transaction.getCategory().getCategoryType() == 0) {
            wallet.setAmount(wallet.getAmount().subtract(transactionDto.getAmount()));
        }
        walletRepository.save(wallet);
        transactionRepository.save(transaction);

    }
}
