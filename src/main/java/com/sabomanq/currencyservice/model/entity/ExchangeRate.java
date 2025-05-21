package com.sabomanq.currencyservice.model.entity;

public class ExchangeRate {
    public int id;
    public int baseCurrencyId;
    public int targetCurrencyId;
    public float exchangeRate;

    public ExchangeRate(int id, int baseCurrencyId, int targetCurrencyId, float exchangeRate) {
        this.id = id;
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;
        this.exchangeRate = exchangeRate;
    }
}
