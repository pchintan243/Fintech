package com.fintech.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "tbwallets")
@Getter
@Setter
@SuperBuilder
public class Wallet extends BaseAuditEntity {

    protected Wallet() {
        this.balance = BigDecimal.ZERO;
        this.availableBalance = BigDecimal.ZERO;
        this.status = WalletStatus.ACTIVE;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "walletidp")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "useridf", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currencyidf", nullable = false)
    private Currency currency;

    @Column(name = "balance", precision = 18, scale = 2, nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "availablebalance", precision = 18, scale = 2, nullable = false)
    private BigDecimal availableBalance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private WalletStatus status = WalletStatus.ACTIVE;

    @Column(name = "walletnumber", nullable = false, unique = true)
    private String walletNumber;

    public enum WalletStatus {
        ACTIVE, FROZEN, CLOSED
    }
}
