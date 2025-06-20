package com.sabomanq.currencyservice.model.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeRate {
    public int id;
    public Currency baseCurrency;
    public Currency targetCurrency;
    public BigDecimal rate;

    public ExchangeRate(int id, Currency baseCurrency, Currency targetCurrency, BigDecimal exchangeRate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = exchangeRate;
    }

    public void invert() {
        Currency temp = baseCurrency;
        baseCurrency = targetCurrency;
        targetCurrency = temp;

        rate = BigDecimal.ONE.divide(rate, 2, RoundingMode.HALF_UP);
    }
}
