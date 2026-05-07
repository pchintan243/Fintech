package com.fintech.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fintech.backend.enums.AccountTier;
import com.fintech.backend.enums.UserKycStatus;
import com.fintech.backend.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tbusers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"password"})
@ToString(exclude = {"password"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User extends BaseAuditEntity {

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
    @Builder.Default
    private UserKycStatus kycStatus = UserKycStatus.UNVERIFIED;

    @Enumerated(EnumType.STRING)
    @Column(name = "accounttier", nullable = false)
    @Builder.Default
    private AccountTier accountTier = AccountTier.FREE;

    @Column(name = "transactionlimit", precision = 18, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal transactionLimit = new BigDecimal("1000.00");

    @Column(name = "country")
    private String country;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;
}
