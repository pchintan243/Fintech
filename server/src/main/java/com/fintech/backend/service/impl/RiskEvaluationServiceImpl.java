package com.fintech.backend.service.impl;

import com.fintech.backend.entity.Transaction;
import com.fintech.backend.enums.NotificationType;
import com.fintech.backend.enums.RiskFlagType;
import com.fintech.backend.enums.RiskSeverity;
import com.fintech.backend.repository.TransactionRepository;
import com.fintech.backend.service.NotificationService;
import com.fintech.backend.service.RiskEvaluationService;
import com.fintech.backend.service.RiskFlagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RiskEvaluationServiceImpl implements RiskEvaluationService {

    private final TransactionRepository transactionRepository;
    private final RiskFlagService riskFlagService;
    private final NotificationService notificationService;

    private static final BigDecimal HIGH_VALUE_THRESHOLD = new BigDecimal("10000");
    private static final BigDecimal AML_THRESHOLD = new BigDecimal("50000");
    private static final BigDecimal VOLUME_THRESHOLD = new BigDecimal("25000");
    private static final int VELOCITY_COUNT = 5;
    private static final int VELOCITY_WINDOW_HOURS = 1;
    private static final int VOLUME_WINDOW_HOURS = 24;

    @Override
    public void evaluate(Transaction transaction) {
        Long userId = transaction.getWallet().getUser().getId();
        Long walletId = transaction.getWallet().getId();
        BigDecimal amount = transaction.getAmount();

        if (amount.compareTo(AML_THRESHOLD) >= 0) {
            createFlag(userId, RiskFlagType.AML_ALERT, RiskSeverity.CRITICAL,
                    "AML threshold exceeded: transaction of " + amount + " " + transaction.getCurrency(),
                    transaction.getId());
            notifyUser(userId, NotificationType.RISK_ALERT,
                    "AML Alert: High-Value Transaction Detected",
                    "A transaction of " + amount + " " + transaction.getCurrency() + " has been flagged for AML review. Our compliance team will investigate shortly.");
        } else if (amount.compareTo(HIGH_VALUE_THRESHOLD) >= 0) {
            createFlag(userId, RiskFlagType.HIGH_VALUE, RiskSeverity.HIGH,
                    "High-value transaction detected: " + amount + " " + transaction.getCurrency(),
                    transaction.getId());
            notifyUser(userId, NotificationType.HIGH_VALUE_TRANSACTION,
                    "High-Value Transaction Processed",
                    "A transaction of " + amount + " " + transaction.getCurrency() + " has been processed. If this was not authorized by you, please contact support immediately.");
        }

        LocalDateTime velocitySince = LocalDateTime.now().minusHours(VELOCITY_WINDOW_HOURS);
        long txCount = transactionRepository.countTransactionsSince(walletId, velocitySince);
        if (txCount > VELOCITY_COUNT) {
            createFlag(userId, RiskFlagType.VELOCITY_CHECK, RiskSeverity.MEDIUM,
                    "Unusual transaction velocity: " + txCount + " transactions in " + VELOCITY_WINDOW_HOURS + " hour(s)",
                    transaction.getId());
            notifyUser(userId, NotificationType.RISK_ALERT,
                    "Unusual Activity Detected",
                    "An unusually high number of transactions (" + txCount + ") have been recorded on your wallet in the last hour. If this is not you, please secure your account.");
        }

        LocalDateTime volumeSince = LocalDateTime.now().minusHours(VOLUME_WINDOW_HOURS);
        BigDecimal volume = transactionRepository.sumVolumePerWalletSince(walletId, volumeSince);
        if (volume.compareTo(VOLUME_THRESHOLD) >= 0) {
            createFlag(userId, RiskFlagType.SUSPICIOUS_PATTERN, RiskSeverity.HIGH,
                    "Daily volume threshold exceeded: " + volume + " " + transaction.getCurrency() + " in " + VOLUME_WINDOW_HOURS + " hours",
                    transaction.getId());
            notifyUser(userId, NotificationType.RISK_ALERT,
                    "Volume Alert: Unusual Transaction Volume",
                    "Your wallet has processed " + volume + " " + transaction.getCurrency() + " in the last 24 hours, which is above our monitoring threshold. This has been flagged for review.");
        }
    }

    private void createFlag(Long userId, RiskFlagType type, RiskSeverity severity, String description, Long transactionId) {
        try {
            riskFlagService.create(userId, type, severity, description, transactionId);
        } catch (Exception e) {
            // Log but don't fail the transaction
        }
    }

    private void notifyUser(Long userId, NotificationType type, String title, String message) {
        try {
            notificationService.create(userId, type, title, message);
        } catch (Exception e) {
            // Log but don't fail the transaction
        }
    }
}
