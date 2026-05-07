package com.fintech.backend.entity;


import com.fintech.backend.config.AuditListener;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.EntityListeners;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditListener.class)
@MappedSuperclass
public class BaseAuditEntity {

    @Column(name = "createdat", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "createdbyidf", nullable = false, updatable = false)
    private Long createdByIdf;

    @Column(name = "lastmodified", nullable = true)
    private LocalDateTime lastModified;

    @Column(name = "lastmodifiedbyidf", nullable = true)
    private Long lastModifiedByIdf;
}
