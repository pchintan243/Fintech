package com.fintech.backend.service.impl;

import com.fintech.backend.entity.Transaction;
import com.fintech.backend.entity.Wallet;
import com.fintech.backend.repository.TransactionRepository;
import com.fintech.backend.repository.UserRepository;
import com.fintech.backend.repository.WalletRepository;
import com.fintech.backend.service.TransactionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, WalletRepository walletRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Transaction> getWalletTransactions(Long walletId) {
        return transactionRepository.findByWalletIdOrderByCreatedAtDesc(walletId);
    }

    @Override
    public List<Transaction> getUserTransactions(Long userId) {
        List<Wallet> wallets = walletRepository.findByUserId(userId);
        List<Transaction> transactions = new ArrayList<>();
        for (Wallet wallet : wallets) {
            transactions.addAll(transactionRepository.findByWalletIdOrderByCreatedAtDesc(wallet.getId()));
        }
        return transactions.stream()
                .sorted((t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getDashboardStats(Long userId) {
        List<Wallet> wallets = walletRepository.findByUserId(userId);
        BigDecimal totalBalance = wallets.stream()
                .map(Wallet::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> stats = new HashMap<>();
        stats.put("activeWallets", wallets.size());
        stats.put("totalVolume", totalBalance);
        stats.put("monthlyVolume", totalBalance.doubleValue() > 0 ? totalBalance.doubleValue() : 50000.0); // using total balance or default 50k as mock
        stats.put("totalUsers", userRepository.findAll().size());
        stats.put("openRiskFlags", 1); // Mocked for now to match pendingKyc
        stats.put("recentTransactions", getUserTransactions(userId).stream().limit(5).collect(Collectors.toList()));

        return stats;
    }
}
