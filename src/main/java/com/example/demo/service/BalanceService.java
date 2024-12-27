package com.example.demo.service;

import com.example.demo.model.Balance;

import java.math.BigDecimal;

public interface BalanceService {
    Balance getOrCreate(String currency);

    void deposit(String currencyCode, BigDecimal amount);

    void withdraw(String currencyCode, BigDecimal amount);
}
