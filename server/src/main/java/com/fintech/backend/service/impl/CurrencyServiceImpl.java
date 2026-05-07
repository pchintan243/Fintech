package com.fintech.backend.service.impl;

import com.fintech.backend.dto.CreateCurrencyRequestDTO;
import com.fintech.backend.dto.CurrencyProjection;
import com.fintech.backend.dto.UpdateCurrencyRequestDTO;
import com.fintech.backend.entity.Currency;
import com.fintech.backend.repository.CurrencyRepository;
import com.fintech.backend.service.CurrencyService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    @Override
    public List<Currency> getActiveCurrencies() {
        return currencyRepository.findByIsActiveTrue();
    }

    @Override
    public Optional<Currency> getCurrencyById(Long id) {
        return currencyRepository.findById(id);
    }

    @Override
    public Optional<Currency> getCurrencyByCode(String code) {
        return currencyRepository.findByCode(code);
    }

    @Override
    public Currency createCurrency(CreateCurrencyRequestDTO dto) {
        if (currencyRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Currency with code '" + dto.getCode() + "' already exists");
        }

        Currency currency = Currency.builder()
                .code(dto.getCode().toUpperCase())
                .name(dto.getName())
                .symbol(dto.getSymbol())
                .decimals(dto.getDecimals() != null ? dto.getDecimals() : 2)
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .exchangeRate(dto.getExchangeRate() != null ? new BigDecimal(dto.getExchangeRate()) : BigDecimal.ONE)
                .build();

        return currencyRepository.save(currency);
    }

    @Override
    public Currency updateCurrency(Long id, UpdateCurrencyRequestDTO dto) {
        Currency existing = currencyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Currency not found: " + id));

        if (dto.getName() != null) {
            existing.setName(dto.getName());
        }
        if (dto.getSymbol() != null) {
            existing.setSymbol(dto.getSymbol());
        }
        if (dto.getDecimals() != null) {
            existing.setDecimals(dto.getDecimals());
        }
        if (dto.getIsActive() != null) {
            existing.setIsActive(dto.getIsActive());
        }
        if (dto.getExchangeRate() != null) {
            existing.setExchangeRate(new BigDecimal(dto.getExchangeRate()));
        }

        return currencyRepository.save(existing);
    }

    @Override
    public void deleteCurrency(Long id) {
        if (!currencyRepository.existsById(id)) {
            throw new RuntimeException("Currency not found: " + id);
        }
        currencyRepository.deleteById(id);
    }

    @Override
    public List<CurrencyProjection> getAllCurrenciesProjected() {
        return currencyRepository.findAllProjectedBy();
    }

    @Override
    public List<CurrencyProjection> getActiveCurrenciesProjected() {
        return currencyRepository.findProjectedByIsActiveTrue();
    }

    @Override
    public Optional<CurrencyProjection> getCurrencyByIdProjected(Long id) {
        return currencyRepository.findProjectedById(id);
    }

    @Override
    public CurrencyProjection createCurrencyProjected(CreateCurrencyRequestDTO dto) {
        if (currencyRepository.existsByCode(dto.getCode().toUpperCase())) {
            throw new RuntimeException("Currency with code '" + dto.getCode() + "' already exists");
        }
        Currency currency = Currency.builder()
                .code(dto.getCode().toUpperCase())
                .name(dto.getName())
                .symbol(dto.getSymbol())
                .decimals(dto.getDecimals() != null ? dto.getDecimals() : 2)
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .exchangeRate(dto.getExchangeRate() != null ? new BigDecimal(dto.getExchangeRate()) : BigDecimal.ONE)
                .build();
        Currency saved = currencyRepository.save(currency);
        return currencyRepository.findProjectedById(saved.getId())
                .orElseThrow(() -> new RuntimeException("Failed to load saved currency"));
    }

    @Override
    public CurrencyProjection updateCurrencyProjected(Long id, UpdateCurrencyRequestDTO dto) {
        Currency existing = currencyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Currency not found: " + id));
        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getSymbol() != null) existing.setSymbol(dto.getSymbol());
        if (dto.getDecimals() != null) existing.setDecimals(dto.getDecimals());
        if (dto.getIsActive() != null) existing.setIsActive(dto.getIsActive());
        if (dto.getExchangeRate() != null) existing.setExchangeRate(new BigDecimal(dto.getExchangeRate()));
        currencyRepository.save(existing);
        return currencyRepository.findProjectedById(id)
                .orElseThrow(() -> new RuntimeException("Failed to load updated currency"));
    }
}
