package com.sabomanq.currencyservice.model.dto;

import com.sabomanq.currencyservice.model.entity.ExchangeRateFull;

import java.math.BigDecimal;

public class ExchangeRateSumDTO {
    public CurrencyDTO base;
    public CurrencyDTO target;
    public BigDecimal rate;
    public BigDecimal amount;
    public BigDecimal convertedAmount;

    public ExchangeRateSumDTO(CurrencyDTO base, CurrencyDTO target, BigDecimal rate, BigDecimal amount) {
        this.base = base;
        this.target = target;
        this.rate = rate;
        this.amount = amount;
        this.convertedAmount = amount.multiply(rate);
    }

    public ExchangeRateSumDTO(ExchangeRateFull exchangeRateInfo, BigDecimal amount, BigDecimal convertedAmount) {
        this.base = new CurrencyDTO(exchangeRateInfo.baseCurrency);
        this.target = new CurrencyDTO(exchangeRateInfo.targetCurrency);
        this.rate = exchangeRateInfo.exchangeRate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }
}
