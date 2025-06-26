package com.sabomanq.currencyservice.http;

import com.sabomanq.currencyservice.model.form.CurrencyForm;
import com.sabomanq.currencyservice.model.form.ExchangeListForm;
import com.sabomanq.currencyservice.model.form.ExchangeRateForm;
import com.sabomanq.currencyservice.model.form.ExchangeTransactionForm;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

public class RequestParser {
    public static boolean isNullOrBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    private static boolean isUrlencoded(HttpServletRequest req) {
        return req.getContentType() != null && req.getContentType().startsWith("application/x-www-form-urlencoded");
    }

    public static CurrencyForm getPostCurrency(HttpServletRequest req) throws BadRequest {
        if (!isUrlencoded(req)) {
            throw new BadRequest("Invalid request.");
        }

        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");

        if (isNullOrBlank(name) || isNullOrBlank(code) || isNullOrBlank(sign)) {
            throw new BadRequest("Required form field is missing.");
        }
        return new CurrencyForm(code, name, sign);
    }

    public static ExchangeRateForm getExchangeRateForm(HttpServletRequest req) throws BadRequest {
        if (!isUrlencoded(req)) {
            throw new BadRequest("Invalid request");
        }

        String rate = req.getParameter("rate");
        if (isNullOrBlank(rate)) {
            throw new BadRequest("Required form field is missing.");
        }

        return new ExchangeRateForm(Float.parseFloat(rate));
    }

    public static ExchangeListForm getExchangePost(HttpServletRequest req) throws BadRequest {
        if (!isUrlencoded(req)) {
            throw new BadRequest("Invalid request");
        }

        Map<String, String[]> map = req.getParameterMap();
        if (!map.containsKey("baseCurrencyCode") || !map.containsKey("targetCurrencyCode") || !map.containsKey("rate"))  {
            throw new BadRequest("Required form field is missing.");
        }

        String baseCode = req.getParameter("baseCurrencyCode");
        String targetCode = req.getParameter("targetCurrencyCode");
        float rate;
        try {
            rate = Float.parseFloat(req.getParameter("rate"));
        } catch (NumberFormatException err) {
            throw new BadRequest("Invalid rate");
        }

        return new ExchangeListForm(baseCode, targetCode, rate);
    }

    public static ExchangeTransactionForm getExchangeTransactionForm(HttpServletRequest request) throws BadRequest {
        String fromCode = request.getParameter("from");
        String toCode = request.getParameter("to");
        String amount = request.getParameter("amount");

        if (isNullOrBlank(fromCode) || isNullOrBlank(toCode) || isNullOrBlank(amount))  {
            throw new BadRequest("Required form field is missing.");
        }

        return new ExchangeTransactionForm(fromCode, toCode, new BigDecimal(amount));
    }
}
