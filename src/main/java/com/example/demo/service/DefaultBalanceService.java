package com.example.demo.service;

import com.example.demo.exception.InsufficientBalanceException;
import com.example.demo.model.Balance;
import com.example.demo.repository.BalanceRepository;
import com.example.demo.repository.entity.BalanceEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DefaultBalanceService implements BalanceService {

    private final BalanceRepository balanceRepository;

    @Override
    public Balance getOrCreate(String currency) {
        return balanceRepository.findByCurrencyCode(currency)
                .orElseGet(() -> balanceRepository.save(new BalanceEntity(currency)));
    }

    @Override
    @Transactional
    public void withdraw(String currencyCode, BigDecimal amount) {
        BalanceEntity balance = getBalanceEntity(currencyCode);

        if (balance.getAmount().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance for withdrawal: " + amount);
        }

        balance.setAmount(balance.getAmount().subtract(amount));
        balanceRepository.save(balance);
    }

    @Override
    @Transactional
    public void deposit(String currencyCode, BigDecimal amount) {
        BalanceEntity balance = getBalanceEntity(currencyCode);
        balance.setAmount(balance.getAmount().add(amount));
        balanceRepository.save(balance);
    }

    private BalanceEntity getBalanceEntity(String currencyCode) {
        return balanceRepository.findByCurrencyCode(currencyCode)
                .orElseThrow(() -> new IllegalArgumentException("Balance not found for currency: " + currencyCode));
    }
}
