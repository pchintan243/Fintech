package com.fintech.backend.controller;

import com.fintech.backend.entity.User;
import com.fintech.backend.entity.RiskFlag;
import com.fintech.backend.service.UserService;
import com.fintech.backend.service.RiskFlagService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risk")
public class RiskFlagController {

    private final RiskFlagService riskFlagService;
    private final UserService userService;

    public RiskFlagController(RiskFlagService riskFlagService, UserService userService) {
        this.riskFlagService = riskFlagService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<RiskFlag>> getUserRiskFlags(Authentication authentication) {
        User user;
        if (authentication == null || authentication.getName().equals("anonymousUser")) {
            user = userService.getAllUsers().get(0);
        } else {
            String email = authentication.getName();
            user = userService.getCurrentUser(email);
        }
        return ResponseEntity.ok(riskFlagService.getUserRiskFlags(user.getId()));
    }
}
