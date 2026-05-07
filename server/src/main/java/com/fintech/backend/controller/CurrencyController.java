package com.fintech.backend.controller;

import com.fintech.backend.dto.CreateCurrencyRequestDTO;
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
    public ResponseEntity<List<Currency>> getAllCurrencies() {
        return ResponseEntity.ok(currencyService.getAllCurrencies());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Currency>> getActiveCurrencies() {
        return ResponseEntity.ok(currencyService.getActiveCurrencies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Currency> getCurrencyById(@PathVariable Long id) {
        return currencyService.getCurrencyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Currency> createCurrency(@RequestBody CreateCurrencyRequestDTO dto) {
        return ResponseEntity.ok(currencyService.createCurrency(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Currency> updateCurrency(@PathVariable Long id, @RequestBody UpdateCurrencyRequestDTO dto) {
        return ResponseEntity.ok(currencyService.updateCurrency(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable Long id) {
        currencyService.deleteCurrency(id);
        return ResponseEntity.noContent().build();
    }
}
