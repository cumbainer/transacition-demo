package com.example.demo.exception;

public class DuplicateReferenceException extends RuntimeException {
    public DuplicateReferenceException(String reference) {
        super(String.format("Transaction with reference '%s' already exists.", reference));
    }
}
