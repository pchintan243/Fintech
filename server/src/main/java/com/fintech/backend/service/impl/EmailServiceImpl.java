package com.fintech.backend.service.impl;

import com.fintech.backend.entity.User;
import com.fintech.backend.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.mail.from:noreply@fintech.com}")
    private String fromAddress;

    @Value("${app.mail.enabled:true}")
    private boolean mailEnabled;

    @Override
    @Async
    public void sendWelcomeEmail(User user, String rawPassword) {
        if (!mailEnabled) {
            log.info("Mail disabled — skipping welcome email for {}", user.getEmail());
            return;
        }

        try {
            Context context = new Context();
            context.setVariables(Map.of(
                    "user", user,
                    "rawPassword", rawPassword
            ));

            String htmlContent = templateEngine.process("welcome-email", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromAddress);
            helper.setTo(user.getEmail());
            helper.setSubject("Welcome to Fintech Dashboard — Your Account Credentials");
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Welcome email sent to {}", user.getEmail());
        } catch (MessagingException e) {
            log.error("Failed to send welcome email to {}: {}", user.getEmail(), e.getMessage());
        }
    }
}
