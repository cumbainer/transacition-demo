package com.example.demo.service;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.exception.DuplicateReferenceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.TransactionException;
import com.example.demo.mapper.TransactionEntityMapper;
import com.example.demo.messaging.kafka.TransactionEventPublisher;
import com.example.demo.model.Balance;
import com.example.demo.model.Transaction;
import com.example.demo.model.TransactionStatus;
import com.example.demo.model.TransactionType;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.entity.TransactionEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
/*
    This service in written in Clean Code fashion, suggested by Robert C. Martin.
    Arguably not the best approach, depending on company's code conventions
 */
public class DefaultTransactionService implements TransactionService {

    private static final TransactionStatus INITIAL_TRANSACTION_STATUS = TransactionStatus.NEW;
    private final BalanceService balanceService;
    private final TransactionEntityMapper transactionEntityMapper;
    private final TransactionRepository transactionRepository;
    private final TransactionEventPublisher transactionEventPublisher;

    @Override
    @Transactional
    public Transaction create(TransactionType type, String reference, BigDecimal amount, String currency) {
        log.debug("Creating transaction: type={}, reference={}, amount={}, currency={}", type, reference, amount, currency);

        validateDuplicateReference(reference);

        Balance balance = balanceService.getOrCreate(currency);
        long balanceId = balance.getId();

        TransactionEntity transaction = transactionRepository.save(new TransactionEntity(
                balanceId,
                reference,
                type,
                amount,
                currency
        ));
        transaction.setStatus(INITIAL_TRANSACTION_STATUS);

        adjustBalanceOnCreate(type, balance, amount);

        TransactionEntity savedTransaction = transactionRepository.save(transaction);
        TransactionDTO savedTransactionDTO = transactionEntityMapper.toDto(savedTransaction);
        transactionEventPublisher.publishTransactionCreatedEvent(savedTransactionDTO);

        log.info("Transaction created successfully: {}", savedTransaction);
        return savedTransaction;
    }

    /*
        Methods toSuccess, toError, toProcessing could me merge into method updateTransactionStatus.
        Will be very useful, when more non-final transaction statuses will be added.
        But TransactionStatus is stable component, then it's better to split into different methods.
     */
    @Override
    @Transactional
    public Transaction toSuccess(long id) {
        log.debug("Updating transaction status to SUCCESS: id={}", id);
        TransactionEntity transaction = getById(id);

        String statusErrorMessage = getStatusErrorMessage(transaction, TransactionStatus.SUCCESS);
        if(statusErrorMessage != null) {
            throw new TransactionException(statusErrorMessage);
        }

        transaction.setStatus(TransactionStatus.SUCCESS);
        TransactionEntity updatedTransaction = transactionRepository.save(transaction);

        Balance balance = balanceService.getOrCreate(transaction.getCurrency());
        adjustBalanceOnStatusUpdate(transaction, balance, TransactionStatus.SUCCESS);

        TransactionDTO transactionDTO = transactionEntityMapper.toDto(updatedTransaction);
        transactionEventPublisher.publishTransactionUpdatedEvent(transactionDTO);

        log.info("Transaction {} status updated to SUCCESS", id);
        return transaction;
    }

    @Override
    @Transactional
    public Transaction toError(long id) {
        log.debug("Updating transaction status to ERROR: id={}", id);
        TransactionEntity transaction = getById(id);

        String statusErrorMessage = getStatusErrorMessage(transaction, TransactionStatus.ERROR);
        if(statusErrorMessage != null) {
            throw new TransactionException(statusErrorMessage);
        }

        transaction.setStatus(TransactionStatus.ERROR);
        TransactionEntity updatedTransaction = transactionRepository.save(transaction);

        Balance balance = balanceService.getOrCreate(transaction.getCurrency());
        adjustBalanceOnStatusUpdate(transaction, balance, TransactionStatus.ERROR);

        TransactionDTO transactionDTO = transactionEntityMapper.toDto(updatedTransaction);
        transactionEventPublisher.publishTransactionUpdatedEvent(transactionDTO);

        log.info("Transaction {} status updated to ERROR", id);
        return transaction;
    }

    @Override
    public Transaction toProcessing(long id) {
        log.debug("Updating transaction status to PROCESSING: id={}", id);
        TransactionEntity transaction = getById(id);

        String statusErrorMessage = getStatusErrorMessage(transaction, TransactionStatus.PROCESSING);
        if(statusErrorMessage != null) {
            throw new TransactionException(statusErrorMessage);
        }

        transaction.setStatus(TransactionStatus.PROCESSING);
        TransactionEntity updatedTransaction = transactionRepository.save(transaction);

        TransactionDTO transactionDTO = transactionEntityMapper.toDto(updatedTransaction);
        transactionEventPublisher.publishTransactionUpdatedEvent(transactionDTO);

        log.info("Transaction {} status updated to PROCESSING", id);
        return transaction;
    }

    @Override
    public Optional<Transaction> find(long id) {
        log.debug("Finding transaction by id: {}", id);
        return transactionRepository.findById(id).map(transaction -> transaction);
    }

    private void validateDuplicateReference(String reference) {
        if (transactionRepository.existsByReference(reference)) {
            log.error("Duplicate transaction reference detected: {}", reference);
            throw new DuplicateReferenceException(reference);
        }
    }

    private TransactionEntity getById(long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id " + id));
    }

    private String getStatusErrorMessage(TransactionEntity transaction, TransactionStatus newStatus) {
        if (transaction.getStatus() == newStatus) {
            log.info("Transaction {} is already in {} status. No update performed.", transaction.getId(), newStatus);
            return "Transaction is already in status: " + newStatus;
        }

        if (isFinalStatus(transaction.getStatus())) {
            log.error("Cannot update transaction {} as it is already in a final status: {}",
                    transaction.getId(), transaction.getStatus());
            return
                    String.format("Cannot update transaction %s as it is already in a final status: %s",
                            transaction.getId(),
                            transaction.getStatus()

                    );
        }
        return null;
    }

    private void adjustBalanceOnCreate(TransactionType type, Balance balance, BigDecimal amount) {
        switch (type) {
            case WITHDRAWAL:
                log.debug("Processing withdrawal: Subtracting amount {} from balance {}", amount, balance.getId());
                balanceService.withdraw(balance.getCurrencyCode(), amount);
                break;
            case DEPOSIT:
                log.debug("Processing deposit creation: No balance adjustment required.");
                break;
            default:
                log.error("Unsupported transaction type: {}", type);
                throw new UnsupportedOperationException("Unsupported transaction type: " + type);
        }
    }

    private void adjustBalanceOnStatusUpdate(TransactionEntity transaction,
                                             Balance balance,
                                             TransactionStatus newStatus) {
        TransactionType type = transaction.getType();
        BigDecimal amount = transaction.getAmount();

        switch (newStatus) {
            case SUCCESS:
                if (type == TransactionType.DEPOSIT) {
                    log.debug("Processing deposit success: Adding amount {} to balance {}", amount, balance.getId());
                    balanceService.deposit(balance.getCurrencyCode(), amount);
                }
                break;
            case ERROR:
                if (type == TransactionType.WITHDRAWAL) {
                    log.debug("Processing withdrawal error: Adding amount {} back to balance {}", amount, balance.getId());
                    balanceService.deposit(balance.getCurrencyCode(), amount);
                }
                break;
            default:
                log.error("Unsupported transaction status: {}", newStatus);
                throw new UnsupportedOperationException("Unsupported transaction status: " + newStatus);
        }
    }

    private boolean isFinalStatus(TransactionStatus status) {
        return status == TransactionStatus.SUCCESS || status == TransactionStatus.ERROR;
    }
}
