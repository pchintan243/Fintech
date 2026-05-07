package com.fintech.backend.service.impl;

import com.fintech.backend.dto.RiskFlagProjection;
import com.fintech.backend.entity.RiskFlag;
import com.fintech.backend.entity.Transaction;
import com.fintech.backend.entity.User;
import com.fintech.backend.enums.RiskFlagStatus;
import com.fintech.backend.enums.RiskFlagType;
import com.fintech.backend.enums.RiskSeverity;
import com.fintech.backend.repository.RiskFlagRepository;
import com.fintech.backend.repository.UserRepository;
import com.fintech.backend.service.RiskFlagService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RiskFlagServiceImpl implements RiskFlagService {

    private final RiskFlagRepository riskFlagRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Override
    public List<RiskFlag> getUserRiskFlags(Long userId) {
        return riskFlagRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<RiskFlag> getAllRiskFlags() {
        return riskFlagRepository.findAll();
    }

    @Override
    @Transactional
    public RiskFlag resolve(Long id, String resolvedBy, String resolution) {
        RiskFlag flag = riskFlagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Risk flag not found: " + id));

        flag.setStatus(RiskFlagStatus.RESOLVED);
        flag.setResolvedAt(java.time.LocalDateTime.now());
        flag.setResolvedBy(resolvedBy);
        flag.setResolution(resolution);

        return riskFlagRepository.save(flag);
    }

    @Override
    public List<RiskFlagProjection> getAllRiskFlagsProjected() {
        return riskFlagRepository.findAllProjectedBy();
    }

    @Override
    public List<RiskFlagProjection> getUserRiskFlagsProjected(Long userId) {
        return riskFlagRepository.findProjectedByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    @Transactional
    public RiskFlag create(Long userId, RiskFlagType type, RiskSeverity severity, String description, Long transactionId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        Transaction transaction = null;
        if (transactionId != null) {
            transaction = entityManager.getReference(Transaction.class, transactionId);
        }

        RiskFlag flag = RiskFlag.builder()
                .user(user)
                .type(type)
                .severity(severity)
                .status(RiskFlagStatus.OPEN)
                .description(description)
                .transaction(transaction)
                .build();

        return riskFlagRepository.save(flag);
    }
}
