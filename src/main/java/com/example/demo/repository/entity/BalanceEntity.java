package com.example.demo.repository.entity;

import com.example.demo.model.Balance;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "balance",
        uniqueConstraints = {
                @UniqueConstraint(name = "balance_currency_unique", columnNames = {"currencyCode"})
        }
)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class BalanceEntity implements Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "amount", precision = 27, scale = 18)
    @Setter
    private BigDecimal amount;

    @Column(name = "currencyCode", nullable = false)
    private String currencyCode;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public BalanceEntity(
            @NonNull final String currencyCode
    ) {
        this.amount = BigDecimal.ZERO;
        this.currencyCode = currencyCode;
    }
}
