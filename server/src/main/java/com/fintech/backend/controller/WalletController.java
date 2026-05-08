package com.fintech.backend.controller;

import com.fintech.backend.dto.CreateWalletRequestDTO;
import com.fintech.backend.dto.WalletProjection;
import com.fintech.backend.entity.User;
import com.fintech.backend.entity.Wallet;
import com.fintech.backend.service.UserService;
import com.fintech.backend.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;
    private final UserService userService;

    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || authentication.getName().equals("anonymousUser")) {
            throw new RuntimeException("Authentication required");
        }
        return userService.getCurrentUser(authentication.getName());
    }

    @GetMapping
    public ResponseEntity<List<WalletProjection>> getUserWallets(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return ResponseEntity.ok(walletService.getUserWalletsProjected(user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletProjection> getWallet(Authentication authentication, @PathVariable Long id) {
        return walletService.getByIdProjected(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Wallet> createWallet(Authentication authentication, @RequestBody CreateWalletRequestDTO dto) {
        User currentUser = getAuthenticatedUser(authentication);
        Long targetUserId;
        if (dto.getUserId() != null && currentUser.getRole().name().equals("ROLE_ADMIN")) {
            targetUserId = dto.getUserId();
        } else {
            targetUserId = currentUser.getId();
        }
        Wallet wallet = walletService.createWalletForUser(targetUserId, dto.getCurrency());
        return ResponseEntity.ok(wallet);
    }
}
