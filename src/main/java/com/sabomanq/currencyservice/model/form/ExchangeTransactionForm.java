package com.sabomanq.currencyservice.model.form;


///  for calc
public class ExchangeTransactionForm {
    public String baseCurrency;
    public String targetCurrency;
    public float amount;

    public ExchangeTransactionForm(String baseCurrency, String targetCurrency, float amount) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.amount = amount;
    }
}
