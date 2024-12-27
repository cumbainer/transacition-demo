package com.example.demo.repository.entity;

import com.example.demo.model.Transaction;
import com.example.demo.model.TransactionStatus;
import com.example.demo.model.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "transaction",
        uniqueConstraints = {
                @UniqueConstraint(name = "transaction_reference_unique", columnNames = {"reference"})
        }
)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class TransactionEntity implements Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "balance_id", nullable = false, updatable = false)
    private BalanceEntity balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, updatable = false)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, updatable = false)
    private TransactionStatus status;

    @Column(name = "reference", length = 64, nullable = false, updatable = false)
    private String reference;

    @Column(name = "amount", precision = 27, scale = 18)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false)
    private String currency;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public TransactionEntity(
            final BalanceEntity balance,
            @NonNull final String reference,
            @NonNull final TransactionType type,
            @NonNull final BigDecimal amount,
            @NonNull final String currency
    ) {
        this.balance = balance;
        this.reference = reference;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
        this.status = TransactionStatus.NEW;
    }
}
