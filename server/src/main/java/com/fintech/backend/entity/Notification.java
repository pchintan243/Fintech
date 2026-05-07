package com.fintech.backend.entity;

import com.fintech.backend.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbnotifications")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseAuditEntity {

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
    @Builder.Default
    private boolean isRead = false;
}
