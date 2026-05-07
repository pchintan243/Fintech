package com.fintech.backend.service.impl;

import com.fintech.backend.dto.TransactionProjection;
import com.fintech.backend.entity.Transaction;
import com.fintech.backend.entity.Wallet;
import com.fintech.backend.enums.RiskFlagStatus;
import com.fintech.backend.enums.TransactionStatus;
import com.fintech.backend.enums.WalletStatus;
import com.fintech.backend.repository.RiskFlagRepository;
import com.fintech.backend.repository.TransactionRepository;
import com.fintech.backend.repository.UserRepository;
import com.fintech.backend.repository.WalletRepository;
import com.fintech.backend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final RiskFlagRepository riskFlagRepository;

    @Override
    public Optional<Transaction> getById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public List<Transaction> getWalletTransactions(Long walletId) {
        return transactionRepository.findByWalletIdOrderByCreatedAtDesc(walletId);
    }

    @Override
    public List<Transaction> getWalletTransactions(Long walletId, TransactionStatus status) {
        return transactionRepository.findByWalletIdAndStatus(walletId, status);
    }

    @Override
    public List<Transaction> getUserTransactions(Long userId) {
        List<Wallet> wallets = walletRepository.findByUserId(userId);
        if (wallets.isEmpty()) return new ArrayList<>();

        List<Long> walletIds = wallets.stream().map(Wallet::getId).collect(Collectors.toList());
        return transactionRepository.findByWalletIds(walletIds);
    }

    @Override
    public List<Transaction> getUserTransactions(Long userId, TransactionStatus status) {
        List<Wallet> wallets = walletRepository.findByUserId(userId);
        if (wallets.isEmpty()) return new ArrayList<>();

        List<Transaction> all = new ArrayList<>();
        for (Wallet wallet : wallets) {
            all.addAll(transactionRepository.findByWalletIdAndStatus(wallet.getId(), status));
        }
        return all.stream()
                .sorted((t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getDashboardStats(Long userId) {
        List<Wallet> wallets = walletRepository.findByUserId(userId);
        if (wallets.isEmpty()) {
            Map<String, Object> empty = new HashMap<>();
            empty.put("activeWallets", 0);
            empty.put("totalVolume", BigDecimal.ZERO);
            empty.put("monthlyVolume", BigDecimal.ZERO);
            empty.put("totalUsers", userRepository.findAll().size());
            empty.put("openRiskFlags", riskFlagRepository.countByStatus(RiskFlagStatus.OPEN));
            empty.put("recentTransactions", new ArrayList<>());
            return empty;
        }

        List<Long> walletIds = wallets.stream().map(Wallet::getId).collect(Collectors.toList());
        BigDecimal totalVolume = transactionRepository.sumCompletedVolume(walletIds);

        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        BigDecimal monthlyVolume = transactionRepository.sumVolumeSince(walletIds, thirtyDaysAgo);

        Map<String, Object> stats = new HashMap<>();
        stats.put("activeWallets", walletRepository.countByStatus(WalletStatus.ACTIVE));
        stats.put("totalVolume", totalVolume);
        stats.put("monthlyVolume", monthlyVolume);
        stats.put("totalUsers", userRepository.findAll().size());
        stats.put("openRiskFlags", riskFlagRepository.countByStatus(RiskFlagStatus.OPEN));
        stats.put("recentTransactions", getUserTransactionsProjected(userId).stream().limit(10).collect(Collectors.toList()));

        return stats;
    }

    @Override
    public Optional<TransactionProjection> getByIdProjected(Long id) {
        return transactionRepository.findProjectedById(id);
    }

    @Override
    public List<TransactionProjection> getWalletTransactionsProjected(Long walletId) {
        return transactionRepository.findProjectedByWalletIdOrderByCreatedAtDesc(walletId);
    }

    @Override
    public List<TransactionProjection> getUserTransactionsProjected(Long userId) {
        List<Wallet> wallets = walletRepository.findByUserId(userId);
        if (wallets.isEmpty()) return new ArrayList<>();
        List<Long> walletIds = wallets.stream().map(Wallet::getId).collect(Collectors.toList());
        return transactionRepository.findProjectedByWalletIds(walletIds);
    }

    @Override
    public List<TransactionProjection> getUserTransactionsProjected(Long userId, TransactionStatus status) {
        List<Wallet> wallets = walletRepository.findByUserId(userId);
        if (wallets.isEmpty()) return new ArrayList<>();
        List<TransactionProjection> all = new ArrayList<>();
        for (Wallet wallet : wallets) {
            all.addAll(transactionRepository.findProjectedByWalletIdAndStatusOrderByCreatedAtDesc(wallet.getId(), status));
        }
        return all;
    }
}
