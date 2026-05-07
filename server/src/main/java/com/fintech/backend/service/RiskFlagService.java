package com.fintech.backend.service;

import com.fintech.backend.dto.RiskFlagProjection;
import com.fintech.backend.entity.RiskFlag;

import com.fintech.backend.enums.RiskFlagType;
import com.fintech.backend.enums.RiskSeverity;

import java.util.List;

public interface RiskFlagService {
    List<RiskFlag> getUserRiskFlags(Long userId);
    List<RiskFlag> getAllRiskFlags();
    RiskFlag resolve(Long id, String resolvedBy, String resolution);

    // Projection-based read methods
    List<RiskFlagProjection> getAllRiskFlagsProjected();
    List<RiskFlagProjection> getUserRiskFlagsProjected(Long userId);

    RiskFlag create(Long userId, RiskFlagType type, RiskSeverity severity, String description, Long transactionId);
}
