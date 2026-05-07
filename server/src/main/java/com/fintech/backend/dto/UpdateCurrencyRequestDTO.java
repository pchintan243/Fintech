package com.fintech.backend.dto;

import lombok.Data;

@Data
public class UpdateCurrencyRequestDTO {
    private String name;
    private String symbol;
    private Integer decimals;
    private Boolean isActive;
    private String exchangeRate;
}
