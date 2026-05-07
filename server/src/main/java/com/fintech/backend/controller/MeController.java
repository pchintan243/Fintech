package com.fintech.backend.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/me")
public class MeController {

    @GetMapping
    public Map<String, Object> getCurrentUser(Authentication authentication) {
        Map<String, Object> details = new HashMap<>();
        if (authentication == null) {
            details.put("authenticated", false);
            return details;
        }

        details.put("authenticated", true);
        details.put("name", authentication.getName());
        details.put("authorities", authentication.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toList()));
        return details;
    }
}
