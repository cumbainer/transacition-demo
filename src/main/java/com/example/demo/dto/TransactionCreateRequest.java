package com.example.demo.dto;

import com.example.demo.model.TransactionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record TransactionCreateRequest(
        @JsonProperty("type")
        @NotNull(message = "Transaction type must not be null")
        TransactionType type,

        @JsonProperty("amount")
        @NotNull(message = "Amount must not be null")
        @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
        BigDecimal amount,

        @JsonProperty("currency")
        @NotBlank(message = "Currency must not be blank")
        @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a 3-letter uppercase code")
        String currency,

        @JsonProperty("reference")
        @NotBlank(message = "Reference must not be blank")
        String reference
) {
}
