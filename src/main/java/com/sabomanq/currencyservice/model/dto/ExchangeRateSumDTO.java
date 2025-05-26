package com.sabomanq.currencyservice.model.dto;

import com.sabomanq.currencyservice.model.entity.ExchangeRateInfo;

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

    public ExchangeRateSumDTO(ExchangeRateInfo exchangeRateInfo, float amount, float convertedAmount) {
        this.base = new CurrencyDTO(exchangeRateInfo.base);
        this.target = new CurrencyDTO(exchangeRateInfo.target);
        this.rate = exchangeRateInfo.rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }
}
