package com.fintech.backend.controller;

import com.fintech.backend.dto.DepositRequestDTO;
import com.fintech.backend.dto.TransactionProjection;
import com.fintech.backend.dto.TransferRequestDTO;
import com.fintech.backend.dto.WithdrawalRequestDTO;
import com.fintech.backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/deposit")
    public ResponseEntity<TransactionProjection> deposit(@RequestBody DepositRequestDTO dto) {
        TransactionProjection tx = paymentService.deposit(
                dto.getWalletId(),
                dto.getAmount(),
                dto.getCurrency(),
                dto.getDescription()
        );
        return ResponseEntity.ok(tx);
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<TransactionProjection> withdraw(@RequestBody WithdrawalRequestDTO dto) {
        TransactionProjection tx = paymentService.withdraw(
                dto.getWalletId(),
                dto.getAmount(),
                dto.getCurrency(),
                dto.getDescription()
        );
        return ResponseEntity.ok(tx);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionProjection> transfer(@RequestBody TransferRequestDTO dto) {
        TransactionProjection tx = paymentService.transfer(
                dto.getFromWalletId(),
                dto.getToWalletId(),
                dto.getAmount(),
                dto.getCurrency(),
                dto.getDescription()
        );
        return ResponseEntity.ok(tx);
    }
}
