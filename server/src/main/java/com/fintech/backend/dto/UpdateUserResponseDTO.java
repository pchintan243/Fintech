package com.fintech.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserResponseDTO {
    private Long id;
    private String email;
    private String fullName;
    private String kycStatus;
    private String accountTier;
    private String role;
}
