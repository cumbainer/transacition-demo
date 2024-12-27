package com.example.demo.mapper;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.repository.document.TransactionDocument;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionDocumentMapper implements BaseMapper<TransactionDocument, TransactionDTO> {
    @Override
    public TransactionDocument toDomain(TransactionDTO transactionDTO) {
        if (transactionDTO == null) {
            return null;
        }

        TransactionDocument transactionDocument = new TransactionDocument();
        transactionDocument.setId(transactionDTO.getId());
        transactionDocument.setReference(transactionDTO.getReference());
        transactionDocument.setType(transactionDTO.getType());
        transactionDocument.setStatus(transactionDTO.getStatus());
        transactionDocument.setAmount(transactionDTO.getAmount());
        transactionDocument.setCurrency(transactionDTO.getCurrency());
        transactionDocument.setCreatedAt(transactionDTO.getCreatedAt());
        transactionDocument.setUpdatedAt(transactionDTO.getUpdatedAt());

        return transactionDocument;
    }

    @Override
    public TransactionDTO toDto(TransactionDocument transactionDocument) {
        return null;
    }

    @Override
    public List<TransactionDocument> toDomains(List<TransactionDTO> transactionDTOS) {
        return List.of();
    }

    @Override
    public List<TransactionDTO> toDtos(List<TransactionDocument> transactionDocuments) {
        return List.of();
    }
}
