package com.example.demo.controller;

import com.example.demo.dto.TransactionCreateRequest;
import com.example.demo.model.Transaction;
import com.example.demo.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody TransactionCreateRequest request) {
        Transaction transaction = transactionService.create(
                request.type(),
                request.reference(),
                request.amount(),
                request.currencyCode()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    /*
        Next 3 methods could be merged into 1 method updateTransactionStatus(id, status)
        Having 1 method over 3 could be beneficial when TransactionStatus enum will often change
        (New non-final transaction statuses will be added). Anyway there will be only 2 final statuses.

     */
    @PatchMapping("/{id}/processing")
    public ResponseEntity<Transaction> markTransactionProcessing(@PathVariable Long id) {
        Transaction updatedTransaction = transactionService.toProcessing(id);
        return ResponseEntity.ok(updatedTransaction);
    }

    @PatchMapping("/{id}/success")
    public ResponseEntity<Transaction> markTransactionSuccess(@PathVariable Long id) {
        Transaction updatedTransaction = transactionService.toSuccess(id);
        return ResponseEntity.ok(updatedTransaction);
    }

    @PatchMapping("/{id}/error")
    public ResponseEntity<Transaction> markTransactionError(@PathVariable Long id) {
        Transaction updatedTransaction = transactionService.toError(id);
        return ResponseEntity.ok(updatedTransaction);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long id) {
        Transaction transaction = transactionService.get(id);
        return ResponseEntity.ok(transaction);
    }
}
