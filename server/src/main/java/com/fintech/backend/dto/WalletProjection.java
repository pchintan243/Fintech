package com.fintech.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface WalletProjection {
    Long getId();
    Long getUserId();

    String getCurrencyCode();
    String getCurrencyName();
    String getCurrencySymbol();

    BigDecimal getBalance();
    BigDecimal getAvailableBalance();
    String getStatus();
    String getWalletNumber();
}
