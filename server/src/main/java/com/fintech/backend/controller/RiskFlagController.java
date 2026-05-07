package com.fintech.backend.controller;

import com.fintech.backend.dto.RiskFlagProjection;
import com.fintech.backend.entity.RiskFlag;
import com.fintech.backend.entity.User;
import com.fintech.backend.enums.RiskFlagType;
import com.fintech.backend.enums.RiskSeverity;
import com.fintech.backend.service.RiskFlagService;
import com.fintech.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/risk")
@RequiredArgsConstructor
public class RiskFlagController {

    private final RiskFlagService riskFlagService;
    private final UserService userService;

    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || authentication.getName().equals("anonymousUser")) {
            throw new RuntimeException("Authentication required");
        }
        return userService.getCurrentUser(authentication.getName());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RiskFlagProjection>> getAllRiskFlags(Authentication authentication) {
        return ResponseEntity.ok(riskFlagService.getAllRiskFlagsProjected());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RiskFlag> createRiskFlag(
            Authentication authentication,
            @RequestBody Map<String, String> body) {
        getAuthenticatedUser(authentication);
        Long userId = Long.parseLong(body.get("userId"));
        RiskFlagType type = RiskFlagType.valueOf(body.get("type"));
        RiskSeverity severity = RiskSeverity.valueOf(body.get("severity"));
        String description = body.get("description");
        RiskFlag created = riskFlagService.create(userId, type, severity, description, null);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/{id}/resolve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RiskFlag> resolveRiskFlag(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        User admin = getAuthenticatedUser(authentication);
        RiskFlag resolved = riskFlagService.resolve(
                id,
                admin.getEmail(),
                body.getOrDefault("resolution", "Resolved by admin")
        );
        return ResponseEntity.ok(resolved);
    }
}
