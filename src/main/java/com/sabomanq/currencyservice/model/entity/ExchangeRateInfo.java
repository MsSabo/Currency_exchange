package com.sabomanq.currencyservice.model.entity;


public class ExchangeRateInfo {
    public int id;
    public Currency base;
    public Currency target;
    public float rate;

    public ExchangeRateInfo(int id, Currency base, Currency target, float rate) {
        this.id = id;
        this.base = base;
        this.target = target;
        this.rate = rate;
    }
}