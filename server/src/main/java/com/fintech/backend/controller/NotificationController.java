package com.fintech.backend.controller;

import com.fintech.backend.entity.User;
import com.fintech.backend.entity.Notification;
import com.fintech.backend.service.UserService;
import com.fintech.backend.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getUserNotifications(Authentication authentication) {
        User user;
        if (authentication == null || authentication.getName().equals("anonymousUser")) {
            user = userService.getAllUsers().get(0);
        } else {
            String email = authentication.getName();
            user = userService.getCurrentUser(email);
        }
        return ResponseEntity.ok(notificationService.getUserNotifications(user.getId()));
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }
}
