package com.fintech.backend.dto;

import lombok.Data;

@Data
public class CreateWalletRequestDTO {
    private Long userId;
    private String currency;
}
