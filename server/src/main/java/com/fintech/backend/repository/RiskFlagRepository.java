package com.fintech.backend.repository;

import com.fintech.backend.entity.RiskFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiskFlagRepository extends JpaRepository<RiskFlag, Long> {
    List<RiskFlag> findByUserId(Long userId);
}
