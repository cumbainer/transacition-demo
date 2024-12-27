package com.example.demo.controller;

import com.example.demo.dto.TransactionCreateRequest;
import com.example.demo.dto.TransactionCreateResponse;
import com.example.demo.model.Transaction;
import com.example.demo.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public TransactionCreateResponse create(@RequestBody @Validated final TransactionCreateRequest dto) {
        log.info("Creating transaction with reference: {}", dto.reference());

        Transaction transaction = transactionService.create(
                dto.type(),
                dto.reference(),
                dto.amount(),
                dto.currency()
        );

        return new TransactionCreateResponse(transaction.getId());
    }
}
