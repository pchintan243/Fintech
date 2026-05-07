package com.fintech.backend.service.impl;

import com.fintech.backend.dto.TransactionProjection;
import com.fintech.backend.entity.Transaction;
import com.fintech.backend.entity.Wallet;
import com.fintech.backend.enums.TransactionStatus;
import com.fintech.backend.enums.TransactionType;
import com.fintech.backend.enums.WalletStatus;
import com.fintech.backend.repository.TransactionRepository;
import com.fintech.backend.repository.WalletRepository;
import com.fintech.backend.service.PaymentService;
import com.fintech.backend.service.RiskEvaluationService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final EntityManager entityManager;
    private final RiskEvaluationService riskEvaluationService;

    @Override
    @Transactional
    public TransactionProjection deposit(Long walletId, String amount, String currency, String description) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found: " + walletId));

        if (wallet.getStatus() != WalletStatus.ACTIVE) {
            throw new RuntimeException("Wallet is not active. Status: " + wallet.getStatus());
        }

        BigDecimal depositAmount = new BigDecimal(amount);
        if (depositAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Deposit amount must be greater than zero");
        }

        String reference = generateReference("DEP");
        while (transactionRepository.findByReference(reference).isPresent()) {
            reference = generateReference("DEP");
        }

        BigDecimal balanceBefore = wallet.getBalance();
        BigDecimal balanceAfter = balanceBefore.add(depositAmount);

        wallet.setBalance(balanceAfter);
        wallet.setAvailableBalance(wallet.getAvailableBalance().add(depositAmount));
        walletRepository.save(wallet);

        // Use EntityManager.getReference to get a lazy proxy instead of passing the managed entity
        var walletRef = entityManager.getReference(Wallet.class, walletId);

        Transaction tx = Transaction.builder()
                .wallet(wallet)
                .type(TransactionType.DEPOSIT)
                .amount(depositAmount)
                .currency(currency)
                .status(TransactionStatus.COMPLETED)
                .description(description)
                .reference(reference)
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .build();

        Transaction saved = transactionRepository.save(tx);
        riskEvaluationService.evaluate(saved);
        return transactionRepository.findProjectedById(saved.getId())
                .orElseThrow(() -> new RuntimeException("Failed to load saved transaction"));
//        return null;
    }

    @Override
    @Transactional
    public TransactionProjection withdraw(Long walletId, String amount, String currency, String description) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet not found: " + walletId));

        if (wallet.getStatus() != WalletStatus.ACTIVE) {
            throw new RuntimeException("Wallet is not active. Status: " + wallet.getStatus());
        }

        BigDecimal withdrawAmount = new BigDecimal(amount);
        if (withdrawAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Withdrawal amount must be greater than zero");
        }

        if (wallet.getAvailableBalance().compareTo(withdrawAmount) < 0) {
            throw new RuntimeException("Insufficient available balance. Available: " + wallet.getAvailableBalance() + ", Requested: " + withdrawAmount);
        }

        String reference = generateReference("WDR");
        while (transactionRepository.findByReference(reference).isPresent()) {
            reference = generateReference("WDR");
        }

        BigDecimal balanceBefore = wallet.getBalance();
        BigDecimal balanceAfter = balanceBefore.subtract(withdrawAmount);

        wallet.setBalance(balanceAfter);
        wallet.setAvailableBalance(wallet.getAvailableBalance().subtract(withdrawAmount));
        walletRepository.save(wallet);

        var walletRef = entityManager.getReference(Wallet.class, walletId);

        Transaction tx = Transaction.builder()
                .wallet(walletRef)
                .type(TransactionType.WITHDRAWAL)
                .amount(withdrawAmount)
                .currency(currency)
                .status(TransactionStatus.COMPLETED)
                .description(description)
                .reference(reference)
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .build();

        Transaction saved = transactionRepository.save(tx);
        riskEvaluationService.evaluate(saved);
        return transactionRepository.findProjectedById(saved.getId())
                .orElseThrow(() -> new RuntimeException("Failed to load saved transaction"));
    }

    @Override
    @Transactional
    public TransactionProjection transfer(Long fromWalletId, Long toWalletId, String amount, String currency, String description) {
        if (fromWalletId.equals(toWalletId)) {
            throw new RuntimeException("Cannot transfer to the same wallet");
        }

        Wallet fromWallet = walletRepository.findById(fromWalletId)
                .orElseThrow(() -> new RuntimeException("Source wallet not found: " + fromWalletId));

        Wallet toWallet = walletRepository.findById(toWalletId)
                .orElseThrow(() -> new RuntimeException("Destination wallet not found: " + toWalletId));

        if (fromWallet.getStatus() != WalletStatus.ACTIVE) {
            throw new RuntimeException("Source wallet is not active");
        }

        if (toWallet.getStatus() != WalletStatus.ACTIVE) {
            throw new RuntimeException("Destination wallet is not active");
        }

        BigDecimal transferAmount = new BigDecimal(amount);
        if (transferAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Transfer amount must be greater than zero");
        }

        if (fromWallet.getAvailableBalance().compareTo(transferAmount) < 0) {
            throw new RuntimeException("Insufficient balance for transfer");
        }

        String reference = generateReference("TRX");
        while (transactionRepository.findByReference(reference).isPresent()) {
            reference = generateReference("TRX");
        }

        BigDecimal fromBefore = fromWallet.getBalance();
        BigDecimal fromAfter = fromBefore.subtract(transferAmount);
        fromWallet.setBalance(fromAfter);
        fromWallet.setAvailableBalance(fromWallet.getAvailableBalance().subtract(transferAmount));
        walletRepository.save(fromWallet);

        BigDecimal toBefore = toWallet.getBalance();
        BigDecimal toAfter = toBefore.add(transferAmount);
        toWallet.setBalance(toAfter);
        toWallet.setAvailableBalance(toWallet.getAvailableBalance().add(transferAmount));
        walletRepository.save(toWallet);

        var fromRef = entityManager.getReference(Wallet.class, fromWalletId);
        var toRef = entityManager.getReference(Wallet.class, toWalletId);

        Transaction debitTx = Transaction.builder()
                .wallet(fromRef)
                .type(TransactionType.TRANSFER)
                .amount(transferAmount)
                .currency(currency)
                .status(TransactionStatus.COMPLETED)
                .description("Transfer to " + toWallet.getWalletNumber() + (description != null ? ": " + description : ""))
                .reference(reference + "-DR")
                .balanceBefore(fromBefore)
                .balanceAfter(fromAfter)
                .counterpartyName(toWallet.getUser().getFullName())
                .counterpartyAccount(toWallet.getWalletNumber())
                .build();
        Transaction savedDebit = transactionRepository.save(debitTx);
        riskEvaluationService.evaluate(savedDebit);

        Transaction creditTx = Transaction.builder()
                .wallet(toRef)
                .type(TransactionType.TRANSFER)
                .amount(transferAmount)
                .currency(currency)
                .status(TransactionStatus.COMPLETED)
                .description("Transfer from " + fromWallet.getWalletNumber() + (description != null ? ": " + description : ""))
                .reference(reference + "-CR")
                .balanceBefore(toBefore)
                .balanceAfter(toAfter)
                .counterpartyName(fromWallet.getUser().getFullName())
                .counterpartyAccount(fromWallet.getWalletNumber())
                .build();

        Transaction saved = transactionRepository.save(creditTx);
        return transactionRepository.findProjectedById(saved.getId())
                .orElseThrow(() -> new RuntimeException("Failed to load saved transaction"));
    }

    private String generateReference(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
