package com.sabomanq.currencyservice.http;

public class BadRequest extends RuntimeException {
    public BadRequest(String message) {
        super(message);
    }
}
