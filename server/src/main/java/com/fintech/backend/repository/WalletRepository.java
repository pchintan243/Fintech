package com.fintech.backend.repository;

import com.fintech.backend.dto.WalletProjection;
import com.fintech.backend.entity.Wallet;
import com.fintech.backend.enums.WalletStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    List<Wallet> findByUserId(Long userId);

    Optional<Wallet> findByWalletNumber(String walletNumber);

    List<Wallet> findByUserIdAndStatus(Long userId, WalletStatus status);

    long countByStatus(WalletStatus status);

    List<WalletProjection> findAllProjectedBy();

    Optional<WalletProjection> findProjectedById(Long id);

    List<WalletProjection> findProjectedByUserId(Long userId);
}
