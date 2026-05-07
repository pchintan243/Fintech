package com.fintech.backend.repository;

import com.fintech.backend.dto.RiskFlagProjection;
import com.fintech.backend.entity.RiskFlag;
import com.fintech.backend.enums.RiskFlagStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiskFlagRepository extends JpaRepository<RiskFlag, Long> {

    List<RiskFlag> findByUserId(Long userId);

    List<RiskFlag> findByUserIdOrderByCreatedAtDesc(Long userId);

    long countByStatus(RiskFlagStatus status);

    List<RiskFlagProjection> findAllProjectedBy();

    List<RiskFlagProjection> findProjectedByUserIdOrderByCreatedAtDesc(Long userId);
}
