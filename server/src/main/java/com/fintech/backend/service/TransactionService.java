package com.fintech.backend.service;

import com.fintech.backend.dto.TransactionProjection;
import com.fintech.backend.entity.Transaction;
import com.fintech.backend.enums.TransactionStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TransactionService {
    Optional<Transaction> getById(Long id);
    List<Transaction> getWalletTransactions(Long walletId);
    List<Transaction> getUserTransactions(Long userId);
    List<Transaction> getUserTransactions(Long userId, TransactionStatus status);
    List<Transaction> getWalletTransactions(Long walletId, TransactionStatus status);
    Map<String, Object> getDashboardStats(Long userId);

    Optional<TransactionProjection> getByIdProjected(Long id);
    List<TransactionProjection> getWalletTransactionsProjected(Long walletId);
    List<TransactionProjection> getUserTransactionsProjected(Long userId);
    List<TransactionProjection> getUserTransactionsProjected(Long userId, TransactionStatus status);
}
