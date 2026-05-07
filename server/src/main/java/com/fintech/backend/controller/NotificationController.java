package com.fintech.backend.controller;

import com.fintech.backend.dto.NotificationProjection;
import com.fintech.backend.entity.Notification;
import com.fintech.backend.entity.User;
import com.fintech.backend.enums.NotificationType;
import com.fintech.backend.service.NotificationService;
import com.fintech.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || authentication.getName().equals("anonymousUser")) {
            throw new RuntimeException("Authentication required");
        }
        return userService.getCurrentUser(authentication.getName());
    }

    @GetMapping
    public ResponseEntity<List<NotificationProjection>> getUserNotifications(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return ResponseEntity.ok(notificationService.getUserNotificationsProjected(user.getId()));
    }

    @PostMapping
    public ResponseEntity<Notification> createNotification(
            Authentication authentication,
            @RequestBody Map<String, String> body) {
        getAuthenticatedUser(authentication);
        Long userId = Long.parseLong(body.get("userId"));
        NotificationType type = NotificationType.valueOf(body.get("type"));
        String title = body.get("title");
        String message = body.get("message");
        Notification created = notificationService.create(userId, type, title, message);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(Authentication authentication, @PathVariable Long id) {
        getAuthenticatedUser(authentication);
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }
}
