package com.fintech.backend.service.impl;

import com.fintech.backend.entity.RiskFlag;
import com.fintech.backend.repository.RiskFlagRepository;
import com.fintech.backend.service.RiskFlagService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiskFlagServiceImpl implements RiskFlagService {

    private final RiskFlagRepository riskFlagRepository;

    public RiskFlagServiceImpl(RiskFlagRepository riskFlagRepository) {
        this.riskFlagRepository = riskFlagRepository;
    }

    @Override
    public List<RiskFlag> getUserRiskFlags(Long userId) {
        return riskFlagRepository.findByUserId(userId);
    }
}
