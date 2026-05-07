package com.fintech.backend.dto;

import lombok.Data;

@Data
public class WithdrawalRequestDTO {
    private Long walletId;
    private String amount;
    private String currency;
    private String description;
}
