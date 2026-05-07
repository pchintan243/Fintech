package com.fintech.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "tbtransactions")
@Getter
@Setter
@SuperBuilder
public class Transaction extends BaseAuditEntity {

    protected Transaction() {
        this.currency = "USD";
        this.status = TransactionStatus.PENDING;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionidp")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walletidf", nullable = false)
    private Wallet wallet;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;

    @Column(name = "amount", precision = 18, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false)
    private String currency = "USD";

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status = TransactionStatus.PENDING;

    @Column(name = "description")
    private String description;

    @Column(name = "reference", nullable = false, unique = true)
    private String reference;

    @Column(name = "counterpartyname")
    private String counterpartyName;

    @Column(name = "counterpartyaccount")
    private String counterpartyAccount;

    @Column(name = "balancebefore", precision = 18, scale = 2, nullable = false)
    private BigDecimal balanceBefore;

    @Column(name = "balanceafter", precision = 18, scale = 2, nullable = false)
    private BigDecimal balanceAfter;

    @Column(name = "metadata", columnDefinition = "json")
    private String metadata;

    public enum TransactionType {
        DEBIT, CREDIT, TRANSFER, DEPOSIT, WITHDRAWAL
    }

    public enum TransactionStatus {
        PENDING, PROCESSING, COMPLETED, FAILED, REFUNDED
    }
}
