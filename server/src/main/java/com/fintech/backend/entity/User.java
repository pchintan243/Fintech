package com.fintech.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "tbusers")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SuperBuilder
public class User extends BaseAuditEntity {

    protected User() {
        this.kycStatus = KycStatus.UNVERIFIED;
        this.accountTier = AccountTier.FREE;
        this.transactionLimit = new BigDecimal("1000.00");
        this.role = Role.ROLE_USER;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "useridp")
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "fullname", nullable = false)
    private String fullName;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "kycstatus", nullable = false)
    private KycStatus kycStatus = KycStatus.UNVERIFIED;

    @Enumerated(EnumType.STRING)
    @Column(name = "accounttier", nullable = false)
    private AccountTier accountTier = AccountTier.FREE;

    @Column(name = "transactionlimit", precision = 18, scale = 2, nullable = false)
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
    private Role role;
}
