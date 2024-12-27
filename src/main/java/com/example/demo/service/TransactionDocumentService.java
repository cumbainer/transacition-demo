package com.example.demo.service;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.mapper.TransactionDocumentMapper;
import com.example.demo.model.Transaction;
import com.example.demo.repository.TransactionDocumentRepository;
import com.example.demo.repository.document.TransactionDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionDocumentService {
    private final TransactionDocumentRepository transactionDocumentRepository;
    private final TransactionDocumentMapper transactionDocumentMapper;


    public void save(TransactionDTO transactionDTO) {
        TransactionDocument transactionDocument = transactionDocumentMapper.toDomain(transactionDTO);
        transactionDocumentRepository.save(transactionDocument);
    }
}
