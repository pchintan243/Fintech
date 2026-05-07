package com.fintech.backend.controller;

import com.fintech.backend.dto.TransactionProjection;
import com.fintech.backend.entity.User;
import com.fintech.backend.enums.TransactionStatus;
import com.fintech.backend.service.TransactionService;
import com.fintech.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;

    private User getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || authentication.getName().equals("anonymousUser")) {
            throw new RuntimeException("Authentication required");
        }
        return userService.getCurrentUser(authentication.getName());
    }

    @GetMapping
    public ResponseEntity<List<TransactionProjection>> getUserTransactions(
            Authentication authentication,
            @RequestParam(required = false) String status) {
        User user = getAuthenticatedUser(authentication);
        List<TransactionProjection> transactions;
        if (status != null && !status.isBlank()) {
            transactions = transactionService.getUserTransactionsProjected(
                    user.getId(),
                    TransactionStatus.valueOf(status.toUpperCase())
            );
        } else {
            transactions = transactionService.getUserTransactionsProjected(user.getId());
        }
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return ResponseEntity.ok(transactionService.getDashboardStats(user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionProjection> getTransaction(
            Authentication authentication,
            @PathVariable Long id) {
        return transactionService.getByIdProjected(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
