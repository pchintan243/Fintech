package com.fintech.backend.service;

import com.fintech.backend.dto.CurrencyProjection;
import com.fintech.backend.entity.Currency;

import java.util.List;
import java.util.Optional;

public interface CurrencyService {
    List<Currency> getAllCurrencies();
    List<Currency> getActiveCurrencies();
    Optional<Currency> getCurrencyById(Long id);
    Optional<Currency> getCurrencyByCode(String code);
    Currency createCurrency(com.fintech.backend.dto.CreateCurrencyRequestDTO dto);
    Currency updateCurrency(Long id, com.fintech.backend.dto.UpdateCurrencyRequestDTO dto);
    CurrencyProjection updateCurrencyProjected(Long id, com.fintech.backend.dto.UpdateCurrencyRequestDTO dto);
    CurrencyProjection createCurrencyProjected(com.fintech.backend.dto.CreateCurrencyRequestDTO dto);
    void deleteCurrency(Long id);

    // Projection-based read methods
    List<CurrencyProjection> getAllCurrenciesProjected();
    List<CurrencyProjection> getActiveCurrenciesProjected();
    Optional<CurrencyProjection> getCurrencyByIdProjected(Long id);
}
