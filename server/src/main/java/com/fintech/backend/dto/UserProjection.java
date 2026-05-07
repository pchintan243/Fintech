package com.fintech.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface UserProjection {
    Long getId();
    String getEmail();
    String getFullName();
    String getPhone();
    String getKycStatus();
    String getAccountTier();
    BigDecimal getTransactionLimit();
    String getCountry();
    String getRole();
}
