package com.sabomanq.currencyservice.model.entity;

import java.math.BigDecimal;

public class ExchangeRateFull {
    public int id;
    public Currency baseCurrencyId;
    public Currency targetCurrencyId;
    public BigDecimal exchangeRate;

    public ExchangeRateFull(int id, Currency baseCurrencyId, Currency targetCurrencyId, BigDecimal exchangeRate) {
        this.id = id;
        this.baseCurrencyId = baseCurrencyId;
        this.targetCurrencyId = targetCurrencyId;
        this.exchangeRate = exchangeRate;
    }
}
