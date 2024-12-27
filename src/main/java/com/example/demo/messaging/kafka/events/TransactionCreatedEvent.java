package com.example.demo.messaging.kafka.events;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.model.Transaction;

public record TransactionCreatedEvent(TransactionDTO transactionDto) implements TransactionEvent {
    @Override
    public Transaction getTransaction() {
        return transactionDto;
    }
}
