package com.fintech.backend.service.impl;

import com.fintech.backend.entity.Currency;
import com.fintech.backend.entity.User;
import com.fintech.backend.entity.Wallet;
import com.fintech.backend.repository.CurrencyRepository;
import com.fintech.backend.repository.UserRepository;
import com.fintech.backend.repository.WalletRepository;
import com.fintech.backend.service.WalletService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final CurrencyRepository currencyRepository;

    public WalletServiceImpl(WalletRepository walletRepository, UserRepository userRepository,
                             CurrencyRepository currencyRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.currencyRepository = currencyRepository;
    }

    @Override
    public List<Wallet> getUserWallets(Long userId) {
        return walletRepository.findByUserId(userId);
    }

    @Override
    public Wallet createWallet(Long userId, String currencyCode) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Currency currency = currencyRepository.findByCode(currencyCode)
                .orElseThrow(() -> new RuntimeException("Currency not found: " + currencyCode));

        Wallet wallet = Wallet.builder()
                .user(user)
                .currency(currency)
                .walletNumber(UUID.randomUUID().toString() + "-" + currencyCode)
                .build();

        return walletRepository.save(wallet);
    }
}
