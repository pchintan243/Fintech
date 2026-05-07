package com.fintech.backend.service;

import com.fintech.backend.entity.Transaction;
import java.util.List;
import java.util.Map;

public interface TransactionService {
    List<Transaction> getWalletTransactions(Long walletId);
    List<Transaction> getUserTransactions(Long userId);
    Map<String, Object> getDashboardStats(Long userId);
}
