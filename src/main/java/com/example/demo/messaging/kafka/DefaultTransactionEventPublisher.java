package com.example.demo.messaging.kafka;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.mapper.TransactionEntityMapper;
import com.example.demo.messaging.kafka.events.TransactionCreatedEvent;
import com.example.demo.messaging.kafka.events.TransactionEvent;
import com.example.demo.messaging.kafka.events.TransactionUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultTransactionEventPublisher implements TransactionEventPublisher {
    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;
    private static final String TRANSACTION_TOPIC = "transaction";

    @Override
    public void publishTransactionCreatedEvent(TransactionDTO createdTransaction) {
        log.info("Sent created transaction: {}", createdTransaction);

        TransactionCreatedEvent createdEvent = new TransactionCreatedEvent(createdTransaction);
        kafkaTemplate.send(TRANSACTION_TOPIC, createdEvent);
    }

    @Override
    public void publishTransactionUpdatedEvent(TransactionDTO updatedTransaction) {
        log.info("Sent updated transaction: {}", updatedTransaction);

        TransactionUpdatedEvent updatedEvent = new TransactionUpdatedEvent(updatedTransaction);
        kafkaTemplate.send(TRANSACTION_TOPIC, updatedEvent);
    }

}
