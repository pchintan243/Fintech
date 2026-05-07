package com.fintech.backend.controller;

import com.fintech.backend.dto.CreateCurrencyRequestDTO;
import com.fintech.backend.dto.CurrencyProjection;
import com.fintech.backend.dto.UpdateCurrencyRequestDTO;
import com.fintech.backend.entity.Currency;
import com.fintech.backend.service.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping
    public ResponseEntity<List<CurrencyProjection>> getAllCurrencies() {
        return ResponseEntity.ok(currencyService.getAllCurrenciesProjected());
    }

    @GetMapping("/active")
    public ResponseEntity<List<CurrencyProjection>> getActiveCurrencies() {
        return ResponseEntity.ok(currencyService.getActiveCurrenciesProjected());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurrencyProjection> getCurrencyById(@PathVariable Long id) {
        return currencyService.getCurrencyByIdProjected(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CurrencyProjection> createCurrency(@RequestBody CreateCurrencyRequestDTO dto) {
        return ResponseEntity.ok(currencyService.createCurrencyProjected(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CurrencyProjection> updateCurrency(@PathVariable Long id, @RequestBody UpdateCurrencyRequestDTO dto) {
        return ResponseEntity.ok(currencyService.updateCurrencyProjected(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable Long id) {
        currencyService.deleteCurrency(id);
        return ResponseEntity.noContent().build();
    }
}
