package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TransactionCreateResponse(
        @JsonProperty(value = "transaction_id")
        long transactionId
) {
}
