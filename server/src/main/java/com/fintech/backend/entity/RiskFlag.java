package com.fintech.backend.entity;

import com.fintech.backend.enums.RiskFlagStatus;
import com.fintech.backend.enums.RiskFlagType;
import com.fintech.backend.enums.RiskSeverity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbriskflag")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"user", "transaction"})
@ToString(exclude = {"user", "transaction"})
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
}
