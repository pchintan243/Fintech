package com.fintech.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.EntityListeners;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Base auditing entity inherited by all entities.
 *
 * Automatically populated by Spring Data JPA Auditing:
 *   - createdAt         : timestamp when the record was first created (NOT NULL)
 *   - lastModified      : timestamp of the most recent update (NULL until first update)
 *   - createdByIdf      : UserIDP of the user who created the record (NOT NULL)
 *   - lastModifiedByIdf : UserIDP of the last user who modified the record (NULL until first update)
 *
 * Uses @SuperBuilder so subclasses can participate in the Lombok builder chain.
 */
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@SuperBuilder
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseAuditEntity {

    protected BaseAuditEntity() {}

    @CreatedDate
    @Column(name = "createdat", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "lastmodified", nullable = true)
    private LocalDateTime lastModified;

    @CreatedBy
    @Column(name = "createdbyidf", nullable = false, updatable = false)
    private Long createdByIdf;

    @LastModifiedBy
    @Column(name = "lastmodifiedbyidf", nullable = true)
    private Long lastModifiedByIdf;
}
