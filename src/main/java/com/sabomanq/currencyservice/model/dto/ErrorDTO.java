package com.sabomanq.currencyservice.model.dto;

public class ErrorDTO {
    public String message;

    public ErrorDTO(String error) {
        this.message = error;
    }
}
