package com.sabomanq.currencyservice.model.dto;


import java.math.BigDecimal;

public class ExchangeRateDTO {
    public int id;
    public CurrencyDTO baseCurrency;
    public CurrencyDTO targetCurrency;
    public BigDecimal rate;

    public ExchangeRateDTO() {
    }

    public ExchangeRateDTO(int id, CurrencyDTO base, CurrencyDTO target, BigDecimal rate) {
        this.baseCurrency = base;
        this.targetCurrency = target;
        this.rate = rate;
        this.id = id;
    }
}
