package com.fintech.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fintech.backend.enums.TransactionStatus;
import com.fintech.backend.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbtransactions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends BaseAuditEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionidp")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "walletidf", nullable = false)
    private Wallet wallet;

    @Column(name = "walletidf", insertable = false, updatable = false)
    private Long walletId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;

    @Column(name = "amount", precision = 18, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status;

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
}
