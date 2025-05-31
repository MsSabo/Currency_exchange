package com.sabomanq.currencyservice.model.form;


import java.math.BigDecimal;

///  for calc
public class ExchangeTransactionForm {
    public String baseCurrency;
    public String targetCurrency;
    public BigDecimal amount;

    public ExchangeTransactionForm(String baseCurrency, String targetCurrency, BigDecimal amount) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.amount = amount;
    }
}
