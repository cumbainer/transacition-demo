package com.example.demo.messaging.kafka.events;

import com.example.demo.model.Transaction;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "eventType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TransactionCreatedEvent.class, name = "TransactionCreatedEvent"),
        @JsonSubTypes.Type(value = TransactionUpdatedEvent.class, name = "TransactionUpdatedEvent")
})
public interface TransactionEvent {
    Transaction getTransaction();
}
