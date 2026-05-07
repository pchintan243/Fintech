package com.fintech.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbriskflag")
@Getter
@Setter
@SuperBuilder
public class RiskFlag extends BaseAuditEntity {

    protected RiskFlag() {
        this.status = RiskFlagStatus.OPEN;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "riskflagidp")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "useridf", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private RiskFlagType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private RiskSeverity severity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RiskFlagStatus status = RiskFlagStatus.OPEN;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transactionidf")
    private Transaction transaction;

    @Column(name = "resolvedat")
    private LocalDateTime resolvedAt;

    @Column(name = "resolvedby")
    private String resolvedBy;

    @Column(name = "resolution")
    private String resolution;

    public enum RiskFlagType {
        VELOCITY_CHECK, HIGH_VALUE, GEOFENCE, SUSPICIOUS_PATTERN, AML_ALERT
    }

    public enum RiskSeverity {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    public enum RiskFlagStatus {
        OPEN, INVESTIGATING, RESOLVED, FALSE_POSITIVE
    }
}
