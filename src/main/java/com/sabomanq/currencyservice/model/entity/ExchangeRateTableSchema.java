package com.sabomanq.currencyservice.model.entity;

public class ExchangeRateTableSchema {
    public static final String TABLE_NAME = "ExchangeRates";
    public static final String ID = "id";
    public static final String BASE_ID = "baseCurrencyId";
    public static final String TARGET_ID = "targetCurrencyId";
    public static final String RATE = "rate";

    private ExchangeRateTableSchema() {};
}
