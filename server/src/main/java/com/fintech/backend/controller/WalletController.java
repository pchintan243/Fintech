package com.fintech.backend.controller;

import com.fintech.backend.entity.User;
import com.fintech.backend.entity.Wallet;
import com.fintech.backend.service.UserService;
import com.fintech.backend.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;
    private final UserService userService;

    public WalletController(WalletService walletService, UserService userService) {
        this.walletService = walletService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Wallet>> getUserWallets(Authentication authentication) {
        User user;
        if (authentication == null || authentication.getName().equals("anonymousUser")) {
            user = userService.getAllUsers().get(0);
        } else {
            String email = authentication.getName();
            user = userService.getCurrentUser(email);
        }
        return ResponseEntity.ok(walletService.getUserWallets(user.getId()));
    }

    @PostMapping
    public ResponseEntity<Wallet> createWallet(Authentication authentication, @RequestBody Map<String, String> request) {
        User user;
        if (authentication == null || authentication.getName().equals("anonymousUser")) {
            user = userService.getAllUsers().get(0);
        } else {
            String email = authentication.getName();
            user = userService.getCurrentUser(email);
        }
        String currency = request.getOrDefault("currency", "USD");
        return ResponseEntity.ok(walletService.createWallet(user.getId(), currency));
    }
}
