package com.example.demo.repository;

import com.example.demo.repository.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    boolean existsByReference(String reference);

}
