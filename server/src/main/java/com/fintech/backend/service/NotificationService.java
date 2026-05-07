package com.fintech.backend.service;

import com.fintech.backend.dto.NotificationProjection;
import com.fintech.backend.entity.Notification;
import com.fintech.backend.enums.NotificationType;

import java.util.List;

public interface NotificationService {
//    List<Notification> getUserNotifications(Long userId);
    Notification createNotification(Long userId, NotificationType type, String title, String message);

    Notification create(Long userId, NotificationType type, String title, String message);
    void markAsRead(Long notificationId);
//    void markAllAsRead(Long userId);

    List<NotificationProjection> getUserNotificationsProjected(Long userId);
}
