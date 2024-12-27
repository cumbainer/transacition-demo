package com.example.demo.messaging.kafka;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.model.Transaction;

public interface TransactionEventPublisher {
    void publishTransactionCreatedEvent(TransactionDTO createdTransaction);
    void publishTransactionUpdatedEvent(TransactionDTO updatedTransaction);
}
