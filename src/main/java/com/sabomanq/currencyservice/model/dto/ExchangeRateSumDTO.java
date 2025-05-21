package com.sabomanq.currencyservice.model.dto;

public class ExchangeRateSumDTO {
    public CurrencyDTO base;
    public CurrencyDTO target;
    public float rate;
    public float amount;
    public float convertedAmount;

    public ExchangeRateSumDTO(CurrencyDTO base, CurrencyDTO target, float rate, float amount) {
        this.base = base;
        this.target = target;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = amount * rate;
    }
}
