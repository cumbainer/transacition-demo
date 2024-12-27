package com.example.demo.mapper;

import com.example.demo.dto.TransactionDTO;
import com.example.demo.repository.entity.TransactionEntity;
import org.springframework.stereotype.Component;

import java.util.List;

//Here it's better to use ModelMapper or MapStruct. For the sake of simplicity - custom impl.
@Component
public class TransactionEntityMapper implements BaseMapper<TransactionEntity, TransactionDTO> {
    @Override
    public TransactionEntity toDomain(TransactionDTO transactionDTO) {
        return null;
    }

    @Override
    public TransactionDTO toDto(TransactionEntity transactionEntity) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transactionEntity.getId());
        dto.setBalanceId(transactionEntity.getBalanceId());
        dto.setType(transactionEntity.getType());
        dto.setStatus(transactionEntity.getStatus());
        dto.setReference(transactionEntity.getReference());
        dto.setAmount(transactionEntity.getAmount());
        dto.setCurrency(transactionEntity.getCurrency());
        dto.setCreatedAt(transactionEntity.getCreatedAt());
        dto.setUpdatedAt(transactionEntity.getUpdatedAt());
        return dto;
    }

    @Override
    public List<TransactionEntity> toDomains(List<TransactionDTO> transactionDTOS) {
        return List.of();
    }

    @Override
    public List<TransactionDTO> toDtos(List<TransactionEntity> transactionEntities) {
        return List.of();
    }
}
