package com.fintech.backend.service;

import com.fintech.backend.entity.RiskFlag;
import java.util.List;

public interface RiskFlagService {
    List<RiskFlag> getUserRiskFlags(Long userId);
}
