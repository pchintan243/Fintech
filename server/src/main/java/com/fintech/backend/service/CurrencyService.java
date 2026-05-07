package com.fintech.backend.service;

import com.fintech.backend.dto.CreateCurrencyRequestDTO;
import com.fintech.backend.dto.UpdateCurrencyRequestDTO;
import com.fintech.backend.entity.Currency;

import java.util.List;
import java.util.Optional;

public interface CurrencyService {
    List<Currency> getAllCurrencies();
    List<Currency> getActiveCurrencies();
    Optional<Currency> getCurrencyById(Long id);
    Optional<Currency> getCurrencyByCode(String code);
    Currency createCurrency(CreateCurrencyRequestDTO dto);
    Currency updateCurrency(Long id, UpdateCurrencyRequestDTO dto);
    void deleteCurrency(Long id);
}
