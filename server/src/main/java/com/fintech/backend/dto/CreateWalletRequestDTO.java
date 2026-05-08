package com.fintech.backend.dto;

import lombok.Data;

@Data
public class CreateWalletRequestDTO {
    private Long userId; // optional — admins can specify, normal users create for themselves
    private String currency;
}
