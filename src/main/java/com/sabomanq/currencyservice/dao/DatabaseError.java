package com.sabomanq.currencyservice.dao;

public class DatabaseError extends RuntimeException {
    public DatabaseError(String message) {
        super(message);
    }
}
