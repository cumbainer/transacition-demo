package com.example.demo.messaging.kafka.events;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.model.Transaction;

public record TransactionUpdatedEvent(TransactionDTO transactionDTO) implements TransactionEvent {
    @Override
    public Transaction getTransaction() {
        return transactionDTO;
    }
}
