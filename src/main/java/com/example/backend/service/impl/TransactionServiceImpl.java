package com.example.backend.service.impl;

import com.example.backend.dto.TransactionDto;
import com.example.backend.dto.TransactionInfoDto;
import com.example.backend.dto.TransactionSimpleDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements ITransactionService {

    private final ITransactionRepo transactionRepository;
    private final ICategoryRepo categoryRepository;
    private final IWalletRepo walletRepository;
    private final IUserRepo userRepository;


    @Override
    public Page<TransactionInfoDto> findAllTransactionByUserId(Long userId, Long categoryId, Integer categoryType, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return transactionRepository.findAllTransactionByUserId(userId, categoryId, categoryType, pageable);
    }

    @Override
    public TransactionDto findTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Transaction not found"));
        return convertToDTO(transaction);
    }

    @Override
    public TransactionDto save(Long userId, TransactionDto transactionDto) {
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
        return convertToDTO(transactionRepository.save(transaction));
    }

    public TransactionDto convertToDTO(Transaction transaction) {
        TransactionDto dto = new TransactionDto();
        dto.setId(transaction.getId());
        dto.setCategoryId(transaction.getCategory().getId());
        dto.setWalletId(transaction.getWallet().getId());
        dto.setAmount(transaction.getAmount());
        dto.setNote(transaction.getNote());
        dto.setDatetime(transaction.getDatetime());
        dto.setCategoryType(transaction.getCategory().getCategoryType());
        return dto;
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

        Category oldCategory = categoryRepository.findById(transaction.getCategory().getId()).orElseThrow(() -> new RuntimeException("Category not found"));
        Wallet oldWallet = walletRepository.findById(transaction.getWallet().getId()).orElseThrow(() -> new RuntimeException("Wallet not found"));
        if (wallet.getId() == oldWallet.getId()) {
            // old la thu - new la chi
            if (transaction.getCategory().getCategoryType() == 1 && category.getCategoryType() == 0) {
                BigDecimal newAmount = oldWallet.getAmount().subtract(transaction.getAmount()).subtract(transactionDto.getAmount());
                wallet.setAmount(newAmount);

            }
            // old la thu - new la thu
            else if (transaction.getCategory().getCategoryType() == 0 && category.getCategoryType() == 1) {
                BigDecimal newAmount = oldWallet.getAmount().subtract(transaction.getAmount()).add(transactionDto.getAmount());
                wallet.setAmount(newAmount);
            }
            // old la chi - new la chi
            else if (transaction.getCategory().getCategoryType() == 0 && category.getCategoryType() == 0) {
                BigDecimal newAmount = oldWallet.getAmount().add(transaction.getAmount()).subtract(transactionDto.getAmount());
                wallet.setAmount(newAmount);
            }
            // old la chi - new la thu
            else if (transaction.getCategory().getCategoryType() == 0 && category.getCategoryType() == 1) {
                BigDecimal newAmount = oldWallet.getAmount().add(transaction.getAmount()).add(transactionDto.getAmount());
                wallet.setAmount(newAmount);
            }
            walletRepository.save(wallet);

        }else{
            // old la thu - new la chi
            if (transaction.getCategory().getCategoryType() == 1 && category.getCategoryType() == 0) {
                BigDecimal oldAmount = oldWallet.getAmount().subtract(transaction.getAmount());
                BigDecimal newAmount = wallet.getAmount().subtract(transactionDto.getAmount());
                oldWallet.setAmount(oldAmount);
                wallet.setAmount(newAmount);

            }
            // old la thu - new la thu
            else if (transaction.getCategory().getCategoryType() == 0 && category.getCategoryType() == 1) {
                BigDecimal oldAmount = oldWallet.getAmount().subtract(transaction.getAmount());
                BigDecimal newAmount = wallet.getAmount().add(transactionDto.getAmount());
                oldWallet.setAmount(oldAmount);
                wallet.setAmount(newAmount);

            }
            // old la chi - new la chi
            else if (transaction.getCategory().getCategoryType() == 0 && category.getCategoryType() == 0) {
                BigDecimal oldAmount = oldWallet.getAmount().add(transaction.getAmount());
                BigDecimal newAmount = wallet.getAmount().subtract(transactionDto.getAmount());
                oldWallet.setAmount(oldAmount);
                wallet.setAmount(newAmount);

            }
            // old la chi - new la thu
            else if (transaction.getCategory().getCategoryType() == 0 && category.getCategoryType() == 1) {
                BigDecimal oldAmount = oldWallet.getAmount().add(transaction.getAmount());
                BigDecimal newAmount = wallet.getAmount().add(transactionDto.getAmount());
                oldWallet.setAmount(oldAmount);
                wallet.setAmount(newAmount);

            }
            walletRepository.save(oldWallet);
            walletRepository.save(wallet);

        }
        transaction.setNote(transactionDto.getNote());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setDatetime(transactionDto.getDatetime());
        transaction.setCategory(category);
        transaction.setWallet(wallet);

        transactionRepository.save(transaction);

    }

    @Override
    public List<TransactionSimpleDto> searchTransactionWithUserId(Long userId, Long categoryId) {
        return transactionRepository.findAllByUserIdAndCategoryId(userId, categoryId);
    }
}
