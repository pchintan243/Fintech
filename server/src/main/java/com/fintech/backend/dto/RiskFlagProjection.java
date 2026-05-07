package com.fintech.backend.dto;

import java.time.LocalDateTime;

public interface RiskFlagProjection {
    Long getId();
    Long getUserId();
    String getType();
    String getSeverity();
    String getStatus();
    String getDescription();
    Long getTransactionId();
    LocalDateTime getResolvedAt();
    String getResolvedBy();
    String getResolution();
}
