package com.fintech.backend.dto;

import lombok.Data;

@Data
public class CreateUserRequestDTO {
    private String fullName;
    private String email;
    private String phone;
    private String country;
}
