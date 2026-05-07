package com.fintech.backend.repository;

import com.fintech.backend.dto.CurrencyProjection;
import com.fintech.backend.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Optional<Currency> findByCode(String code);
    List<Currency> findByIsActiveTrue();
    boolean existsByCode(String code);

    // Projection queries
    List<CurrencyProjection> findAllProjectedBy();
    List<CurrencyProjection> findProjectedByIsActiveTrue();
    Optional<CurrencyProjection> findProjectedById(Long id);
}
