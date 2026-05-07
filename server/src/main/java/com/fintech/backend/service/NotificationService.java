package com.fintech.backend.service;

import com.fintech.backend.entity.Notification;
import java.util.List;

public interface NotificationService {
    List<Notification> getUserNotifications(Long userId);
    void markAsRead(Long notificationId);
}
