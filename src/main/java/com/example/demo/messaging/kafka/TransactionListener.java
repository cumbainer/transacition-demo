package com.example.demo.messaging.kafka;

import com.example.demo.messaging.kafka.events.TransactionCreatedEvent;
import com.example.demo.messaging.kafka.events.TransactionEvent;
import com.example.demo.messaging.kafka.events.TransactionUpdatedEvent;
import com.example.demo.service.TransactionDocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
//This code is completely not refactored and doesn't follow SRP. But i wanted to have 2 different events: Create, Update
public class TransactionListener {
    private final TransactionDocumentService transactionDocumentService;

    @KafkaListener(topics = "transaction", groupId = "demo", containerFactory = "kafkaListenerContainerFactory")
    public void handleTransactionEvent(TransactionEvent event) {
        if (event instanceof TransactionCreatedEvent) {
            handleTransactionCreatedEvent((TransactionCreatedEvent) event);
        } else if (event instanceof TransactionUpdatedEvent) {
            handleTransactionUpdatedEvent((TransactionUpdatedEvent) event);
        } else {
            log.warn("Received unknown event type: {}", event.getClass().getName());
        }
    }

    private void handleTransactionCreatedEvent(TransactionCreatedEvent event) {
        log.info("Handling TransactionCreatedEvent: {}", event.transactionDto());
        transactionDocumentService.save(event.transactionDto());
    }

    private void handleTransactionUpdatedEvent(TransactionUpdatedEvent event) {
        log.info("Handling TransactionUpdatedEvent: {}", event.transactionDTO());
        transactionDocumentService.save(event.transactionDTO());
    }
}

