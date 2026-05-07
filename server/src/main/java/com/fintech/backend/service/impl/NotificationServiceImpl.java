package com.fintech.backend.service.impl;

import com.fintech.backend.dto.NotificationProjection;
import com.fintech.backend.entity.Notification;
import com.fintech.backend.enums.NotificationType;
import com.fintech.backend.repository.NotificationRepository;
import com.fintech.backend.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

//    @Override
//    public List<Notification> getUserNotifications(Long userId) {
//        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
//    }

    @Override
    public Notification createNotification(Long userId, NotificationType type, String title, String message) {
        Notification notification = Notification.builder()
                .userId(userId)
                .type(type)
                .title(title)
                .message(message)
                .isRead(false)
                .build();
        return notificationRepository.save(notification);
    }

    @Override
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

//    @Override
//    public void markAllAsRead(Long userId) {
//        List<Notification> unread = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
//                .stream().filter(n -> !n.isRead()).toList();
//        for (Notification n : unread) {
//            n.setRead(true);
//        }
//        notificationRepository.saveAll(unread);
//    }

    @Override
    public List<NotificationProjection> getUserNotificationsProjected(Long userId) {
        return notificationRepository.findProjectedByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public Notification create(Long userId, NotificationType type, String title, String message) {
        Notification notification = Notification.builder()
                .userId(userId)
                .type(type)
                .title(title)
                .message(message)
                .isRead(false)
                .build();
        return notificationRepository.save(notification);
    }
}
