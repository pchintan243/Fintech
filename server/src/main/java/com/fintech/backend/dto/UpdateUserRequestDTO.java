package com.fintech.backend.dto;

import lombok.Data;

@Data
public class UpdateUserRequestDTO {
    private String kycStatus;
    private String accountTier;
}
