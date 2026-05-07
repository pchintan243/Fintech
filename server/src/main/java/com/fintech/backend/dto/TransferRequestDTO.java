package com.fintech.backend.dto;

import lombok.Data;

@Data
public class TransferRequestDTO {
    private Long fromWalletId;
    private Long toWalletId;
    private String amount;
    private String currency;
    private String description;
}
