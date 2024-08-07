package com.example.backend.repository;

import com.example.backend.dto.WalletDto;
import com.example.backend.model.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IWalletRepo extends JpaRepository<Wallet,Long> {
    @Query(value = "select wu.wallet_id from user u join wallet_users wu on :userId = wu.users_id join wallet w on wu.wallet_id = w.id", nativeQuery = true)
    List<Wallet> findAllByUserId(Long userId);
    @Query(value = "call delete_wallet(:id)",nativeQuery = true)
    void deleteWalletByID(Long id);
}
