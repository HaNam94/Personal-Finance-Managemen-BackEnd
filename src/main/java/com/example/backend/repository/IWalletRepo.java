package com.example.backend.repository;

import com.example.backend.dto.WalletInfoDto;
import com.example.backend.model.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface IWalletRepo extends JpaRepository<Wallet, Long> {
    @Query(value = "SELECT w FROM Wallet w JOIN w.walletRoles wr WHERE wr.user.id = :userId")
    Set<WalletInfoDto> findAllByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT w FROM Wallet w JOIN w.walletRoles wr WHERE w.id = :walletId and wr.user.id = :userId")
    WalletInfoDto findWalletByIdAndUserId(Long walletId, Long userId);

    @Query(value = "SELECT CASE WHEN COUNT(wr) > 0 THEN TRUE ELSE FALSE END FROM WalletUserRole wr WHERE wr.wallet.id = :walletId AND wr.user.id = :userId AND wr.role = 'OWNER'")
    boolean isOwner(@Param("walletId") Long walletId, @Param("userId") Long userId);


//    @Query(value = "select w from User u join WalletUserRole wr on :userId = wr.user.id join Wallet w on wr.wallet.id = w.id")
//    Set<WalletInfoDto> findAllWalletByUserId(Long userId);




}
