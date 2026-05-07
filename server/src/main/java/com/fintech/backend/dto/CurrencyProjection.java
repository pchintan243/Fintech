package com.fintech.backend.dto;

import java.math.BigDecimal;

public interface CurrencyProjection {
    Long getId();
    String getCode();
    String getName();
    String getSymbol();
    Integer getDecimals();
    Boolean getIsActive();
    BigDecimal getExchangeRate();
}
