package com.example.backend.repository;

import com.example.backend.dto.WalletDto;
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
    @Query("SELECT w FROM Wallet w JOIN w.walletRoles wr WHERE wr.user.id = :userId")
    Set<WalletInfoDto> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT w FROM Wallet w JOIN w.walletRoles wr WHERE w.id = :walletId and wr.user.id = :userId")
    WalletInfoDto findWalletByIdAndUserId(Long walletId, Long userId);
}
