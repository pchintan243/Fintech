package com.fintech.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "tbusers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "useridp")
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "fullname", nullable = false)
    private String fullName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "kycstatus", nullable = false)
    @Builder.Default
    private KycStatus kycStatus = KycStatus.UNVERIFIED;

    @Enumerated(EnumType.STRING)
    @Column(name = "accounttier", nullable = false)
    @Builder.Default
    private AccountTier accountTier = AccountTier.FREE;

    @Column(name = "transactionlimit", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal transactionLimit = new BigDecimal("1000.00");

    @Column(name = "country")
    private String country;

    public enum KycStatus {
        UNVERIFIED, PENDING, BASIC, PREMIUM, REJECTED
    }

    public enum AccountTier {
        FREE, BASIC, PREMIUM
    }

    public enum Role {
        ROLE_USER, ROLE_ADMIN
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Builder.Default
    private Role role = Role.ROLE_USER;
}
