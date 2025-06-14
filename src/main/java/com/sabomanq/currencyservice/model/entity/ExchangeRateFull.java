package com.sabomanq.currencyservice.model.entity;

import java.math.BigDecimal;

public class ExchangeRateFull {
    public int id;
    public Currency baseCurrency;
    public Currency targetCurrency;
    public BigDecimal rate;

    public ExchangeRateFull(int id, Currency baseCurrencyId, Currency targetCurrencyId, BigDecimal exchangeRate) {
        this.id = id;
        this.baseCurrency = baseCurrencyId;
        this.targetCurrency = targetCurrencyId;
        this.rate = exchangeRate;
    }
}
