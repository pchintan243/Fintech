package com.fintech.backend.service;

import com.fintech.backend.entity.Wallet;
import java.util.List;

public interface WalletService {
    List<Wallet> getUserWallets(Long userId);
    Wallet createWallet(Long userId, String currency);
}
