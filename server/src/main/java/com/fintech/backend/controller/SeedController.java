package com.fintech.backend.controller;

import com.fintech.backend.entity.User;
import com.fintech.backend.enums.AccountTier;
import com.fintech.backend.enums.UserKycStatus;
import com.fintech.backend.enums.UserRole;
import com.fintech.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/seed")
public class SeedController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SeedController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/admin")
    public String seedAdmin() {
        if (userRepository.findByEmail("admin@fintrack.io").isPresent()) {
            User user = userRepository.findByEmail("admin@fintrack.io").get();
            user.setRole(UserRole.ROLE_ADMIN);
            user.setPassword(passwordEncoder.encode("admin"));
            userRepository.save(user);
            return "Admin user already exists, password reset to admin and role updated to ROLE_ADMIN";
        }

        User admin = User.builder()
                .email("admin@fintrack.io")
                .fullName("System Administrator")
                .password(passwordEncoder.encode("admin"))
                .kycStatus(UserKycStatus.PREMIUM)
                .accountTier(AccountTier.PREMIUM)
                .role(UserRole.ROLE_ADMIN)
                .build();

        userRepository.save(admin);
        return "Admin user created successfully: admin@fintrack.io / admin";
    }

    @GetMapping("/list")
    public List<String> listUsers() {
        return userRepository.findAll().stream()
                .map(u -> u.getEmail() + " (" + u.getRole() + ")")
                .collect(Collectors.toList());
    }
}
