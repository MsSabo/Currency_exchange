package com.sabomanq.currencyservice.model.dto;


import java.math.BigDecimal;

public class ExchangeRateDTO {
    public int id;
    public CurrencyDTO base;
    public CurrencyDTO target;
    public BigDecimal rate;

    public ExchangeRateDTO(int id, CurrencyDTO base, CurrencyDTO target, BigDecimal rate) {
        this.base = base;
        this.target = target;
        this.rate = rate;
        this.id = id;
    }
}
