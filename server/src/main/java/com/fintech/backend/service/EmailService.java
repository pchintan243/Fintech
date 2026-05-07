package com.fintech.backend.service;

import com.fintech.backend.entity.User;

public interface EmailService {
    void sendWelcomeEmail(User user, String rawPassword);
}
