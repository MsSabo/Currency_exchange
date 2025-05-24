package com.sabomanq.currencyservice.model;

import com.sabomanq.currencyservice.model.form.CurrencyForm;
import com.sabomanq.currencyservice.model.form.ExchangeListForm;
import com.sabomanq.currencyservice.model.form.ExchangeRateForm;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Parsing {
    public static CurrencyForm getPostCurrency(HttpServletRequest req) throws IllegalArgumentException {
        if (!isForm(req)) {
            throw new IllegalArgumentException("Invalid request");
        }

        Map<String, String[]> map = req.getParameterMap();
        if (!map.containsKey("name") || !map.containsKey("code") || !map.containsKey("sign"))  {
            throw new IllegalArgumentException("Required form field is missing.");
        }

        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");
        System.out.println(name + " " + code + " " + sign);
        return new CurrencyForm(code, name, sign);
    }

    public static ExchangeRateForm getExchangeRateForm(HttpServletRequest req) throws IOException, IllegalArgumentException {
        if (!isForm(req)) {
            throw new IllegalArgumentException("Invalid request");
        }

        BufferedReader reader = req.getReader();
        String line;
        Map<String, String> data = new HashMap<>();
        while ((line = reader.readLine()) != null) {
            String[] fields = line.split("=");
            String key = fields[0];
            String value = fields[1];
            data.put(key, value);
        }

        if (!data.containsKey("rate")) {
            throw new IllegalArgumentException("Required form field is missing. Rate");
        }

        return new ExchangeRateForm(Float.parseFloat(data.get("rate")));
    }

    public static ExchangeListForm getExchangePost(HttpServletRequest req) throws IllegalArgumentException {
        if (!isForm(req)) {
            throw new IllegalArgumentException("Invalid request");
        }

        Map<String, String[]> map = req.getParameterMap();
        if (!map.containsKey("baseCode") || !map.containsKey("targetCode") || !map.containsKey("rate"))  {
            throw new IllegalArgumentException("Required form field is missing.");
        }

        String baseCode = req.getParameter("baseCode");
        String targetCode = req.getParameter("targetCode");
        float rate = Float.parseFloat(req.getParameter("rate"));

        return new ExchangeListForm(baseCode, targetCode, rate);
    }

    private static boolean isForm(HttpServletRequest req) {
        if (req.getContentType() != null && req.getContentType().equals("application/x-www-form-urlencoded")) {
            return true;
        }
        return false;
    }
}
