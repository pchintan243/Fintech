package com.fintech.backend.service;

import com.fintech.backend.dto.WalletProjection;
import com.fintech.backend.entity.Wallet;

import java.util.List;
import java.util.Optional;

public interface WalletService {
    Optional<Wallet> getById(Long id);
    List<Wallet> getUserWallets(Long userId);
    Wallet createWallet(Long userId, String currency);

    // Projection-based read methods
    Optional<WalletProjection> getByIdProjected(Long id);
    List<WalletProjection> getUserWalletsProjected(Long userId);
    List<WalletProjection> getAllWalletsProjected();
}
