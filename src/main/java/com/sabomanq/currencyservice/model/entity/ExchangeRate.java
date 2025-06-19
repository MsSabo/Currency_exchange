package com.sabomanq.currencyservice.model.entity;

import java.math.BigDecimal;

public class ExchangeRate {
    public int id;
    public Currency baseCurrency;
    public Currency targetCurrency;
    public BigDecimal rate;

    public ExchangeRate(int id, Currency baseCurrencyId, Currency targetCurrencyId, BigDecimal exchangeRate) {
        this.id = id;
        this.baseCurrency = baseCurrencyId;
        this.targetCurrency = targetCurrencyId;
        this.rate = exchangeRate;
    }
}
