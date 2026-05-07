package com.fintech.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tbnotifications")
@Getter
@Setter
@SuperBuilder
public class Notification extends BaseAuditEntity {

    protected Notification() {
        this.isRead = false;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notificationidp")
    private Long id;

    @Column(name = "useridf")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "isread", nullable = false)
    private boolean isRead = false;

    public enum NotificationType {
        HIGH_VALUE_TRANSACTION, RISK_ALERT, KYC_UPDATE, SYSTEM
    }
}
