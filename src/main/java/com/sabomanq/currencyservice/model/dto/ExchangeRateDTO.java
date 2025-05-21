package com.sabomanq.currencyservice.model.dto;


public class ExchangeRateDTO {
    public int id;
    public CurrencyDTO base;
    public CurrencyDTO target;
    public float rate;

    public ExchangeRateDTO(int id, CurrencyDTO base, CurrencyDTO target, float rate) {
        this.base = base;
        this.target = target;
        this.rate = rate;
        this.id = id;
    }
}
