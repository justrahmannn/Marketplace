package com.marketplace.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String message) {
        super(message);
    }

    public InsufficientBalanceException() {
        super("Insufficient balance for this operation");
    }
}
