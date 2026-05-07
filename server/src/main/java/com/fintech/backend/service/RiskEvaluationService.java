package com.fintech.backend.service;

import com.fintech.backend.entity.Transaction;

public interface RiskEvaluationService {
    void evaluate(Transaction transaction);
}
