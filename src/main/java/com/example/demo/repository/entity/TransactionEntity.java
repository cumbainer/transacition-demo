// src/main/java/com/example/demo/repository/entity/TransactionEntity.java

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
        },
        indexes = {
                @Index(name = "idx_transaction_balance_id", columnList = "balance_id"),
                @Index(name = "idx_transaction_type", columnList = "type"),
                @Index(name = "idx_transaction_status", columnList = "status")
        }
)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class TransactionEntity implements Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /*
        Here I have put unidirectional mapping with Balance
        But if BalanceEntity will be in another microservice, then it's wise to just keep balanceId as it was before
     */
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "balance_id", nullable = false, updatable = false)
//    private BalanceEntity balance;
    @Getter
    @Column(name = "balance_id", nullable = false, updatable = false)
    private long balanceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, updatable = false)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Setter
    @Column(name = "status", nullable = false)
    private TransactionStatus status;

    @Column(name = "reference", length = 64, nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String reference;

    @Column(name = "amount", precision = 27, scale = 18)
    private BigDecimal amount;

    @Column(name = "currencyCode", nullable = false)
    private String currency;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    public TransactionEntity(
            final long balanceId,
            @NonNull final String reference,
            @NonNull final TransactionType type,
            @NonNull final BigDecimal amount,
            @NonNull final String currency
    ) {
        this.balanceId = balanceId;
        this.reference = reference;
        this.amount = amount;
        this.currency = currency;
        this.type = type;
        this.status = TransactionStatus.NEW;
    }
}
