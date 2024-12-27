package com.example.demo.dto;

import com.example.demo.model.Balance;
import jakarta.persistence.Access;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceDTO implements Serializable, Balance {
    private long id;
    private BigDecimal amount;
    private String currencyCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
