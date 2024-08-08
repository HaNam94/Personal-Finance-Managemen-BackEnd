package com.example.backend.repository;

import com.example.backend.dto.WalletInfoDto;
import com.example.backend.model.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface IWalletRepo extends JpaRepository<Wallet,Long> {



    @Query(value = "select wu.wallet_id from user u join wallet_users wu on :userId = wu.users_id join wallet w on wu.wallet_id = w.id", nativeQuery = true)
    List<Wallet> findAllByUserId(Long userId);
    @Query(value = "call delete_wallet(:id)",nativeQuery = true)
    void deleteWalletByID(Long id);
    @Query(value = "update wallet set amonut = :amount where id = :id",nativeQuery = true)
    void updateWalletAmount(@Param("id") Long id,@Param("amount") BigDecimal amount);

    @Query("SELECT w FROM Wallet w JOIN w.users u WHERE u.id = :userId")
    Set<WalletInfoDto> findAllWalletByUserId(@Param("userId") Long userId);
}
