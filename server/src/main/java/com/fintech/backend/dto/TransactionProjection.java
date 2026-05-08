package com.fintech.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface TransactionProjection {
    Long getId();
    Long getWalletId();
    String getType();
    BigDecimal getAmount();
    String getCurrency();
    String getStatus();
    String getDescription();
    String getReference();
    String getCounterpartyName();
    String getCounterpartyAccount();
    BigDecimal getBalanceBefore();
    BigDecimal getBalanceAfter();
    LocalDateTime getCreatedAt();
}
