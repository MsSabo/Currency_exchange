package com.sabomanq.currencyservice.dao;

public class UniqueConstraintViolationException extends RuntimeException {
    public UniqueConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
