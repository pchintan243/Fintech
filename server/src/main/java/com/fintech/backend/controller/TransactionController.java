package com.fintech.backend.controller;

import com.fintech.backend.entity.User;
import com.fintech.backend.entity.Transaction;
import com.fintech.backend.service.UserService;
import com.fintech.backend.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;

    public TransactionController(TransactionService transactionService, UserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getUserTransactions(Authentication authentication) {
        User user;
        if (authentication == null || authentication.getName().equals("anonymousUser")) {
            user = userService.getAllUsers().get(0);
        } else {
            String email = authentication.getName();
            user = userService.getCurrentUser(email);
        }
        return ResponseEntity.ok(transactionService.getUserTransactions(user.getId()));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats(Authentication authentication) {
        User user;
        if (authentication == null || authentication.getName().equals("anonymousUser")) {
            user = userService.getAllUsers().get(0);
        } else {
            String email = authentication.getName();
            user = userService.getCurrentUser(email);
        }
        return ResponseEntity.ok(transactionService.getDashboardStats(user.getId()));
    }
}
