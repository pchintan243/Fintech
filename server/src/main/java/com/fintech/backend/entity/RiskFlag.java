package com.fintech.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbriskflag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RiskFlag extends BaseAuditEntity {

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
    @Builder.Default
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
