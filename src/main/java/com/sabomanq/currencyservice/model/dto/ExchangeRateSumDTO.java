package com.sabomanq.currencyservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sabomanq.currencyservice.model.entity.ExchangeRateFull;

import java.math.BigDecimal;

@JsonPropertyOrder({"baseCurrency", "targetCurrency", "rate", "amount", "convertedAmount"})
public class ExchangeRateSumDTO {
    @JsonProperty("baseCurrency")
    public CurrencyDTO base;

    @JsonProperty("targetCurrency")
    public CurrencyDTO target;
    public BigDecimal rate;
    public BigDecimal amount;
    public BigDecimal convertedAmount;

    public ExchangeRateSumDTO(ExchangeRateFull exchangeRateInfo, BigDecimal amount, BigDecimal convertedAmount) {
        this.base = new CurrencyDTO(exchangeRateInfo.baseCurrency);
        this.target = new CurrencyDTO(exchangeRateInfo.targetCurrency);
        this.rate = exchangeRateInfo.rate;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }
}
