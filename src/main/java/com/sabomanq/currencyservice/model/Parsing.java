package com.sabomanq.currencyservice.model;

import com.sabomanq.currencyservice.model.form.CurrencyForm;
import com.sabomanq.currencyservice.model.form.ExchangeListForm;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

public class Parsing {
    public static CurrencyForm getPostCurrency(HttpServletRequest req) {
        if (!isForm(req)) {
            throw new IllegalArgumentException("Invalid request");
        }
        Map<String, String[]> map = req.getParameterMap();

        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + Arrays.toString(entry.getValue()));
        }
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");
        System.out.println(name + " " + code + " " + sign);
        return new CurrencyForm(code, name, sign);
    }

    public static ExchangeListForm getExchangePost(HttpServletRequest req) {
        if (!isForm(req)) {
            throw new IllegalArgumentException("Invalid request");
        }

        String baseCode = req.getParameter("baseCode");
        String targetCode = req.getParameter("targetCode");
        float rate = Float.parseFloat(req.getParameter("rate"));

        return new ExchangeListForm(baseCode, targetCode, rate);
    }

    private static boolean isForm(HttpServletRequest req) {
        if (!req.getContentType().equals("application/x-www-form-urlencoded")) {
            return false;
        }

        return true;
    }
}
