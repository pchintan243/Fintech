package com.fintech.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "tbcurrencies")
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SuperBuilder
public class Currency extends BaseAuditEntity {

    protected Currency() {
        this.decimals = 2;
        this.isActive = true;
        this.exchangeRate = BigDecimal.ONE;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "currencyidp")
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "symbol", nullable = false, length = 5)
    private String symbol;

    @Column(name = "decimals", nullable = false)
    private Integer decimals = 2;

    @Column(name = "isactive", nullable = false)
    private Boolean isActive = true;

    @Column(name = "exchangerate", precision = 18, scale = 6)
    private BigDecimal exchangeRate = BigDecimal.ONE;
}
