package com.fintech.backend.service;

import com.fintech.backend.dto.TransactionProjection;

public interface PaymentService {
    TransactionProjection deposit(Long walletId, String amount, String currency, String description);
    TransactionProjection withdraw(Long walletId, String amount, String currency, String description);
    TransactionProjection transfer(Long fromWalletId, Long toWalletId, String amount, String currency, String description);
}
