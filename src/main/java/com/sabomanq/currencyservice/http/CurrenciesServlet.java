package com.sabomanq.currencyservice.http;

import com.sabomanq.currencyservice.dao.CurrencyDb;
import com.sabomanq.currencyservice.dao.SqliteProvider;
import com.sabomanq.currencyservice.dao.UniqueConstraintViolationException;
import com.sabomanq.currencyservice.model.dto.CurrencyDTO;
import com.sabomanq.currencyservice.model.form.CurrencyForm;
import com.sabomanq.currencyservice.model.Parsing;
import com.sabomanq.currencyservice.service.Currencies;
import flexjson.JSONSerializer;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(value = "/currencies/*")
public class CurrenciesServlet extends HttpServlet {
    private Currencies currencies;

    @Override
    public void init() {
        currencies = new Currencies(new CurrencyDb(new SqliteProvider()));
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo(); // вернёт "/USD"
        Object result;
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                result = currencies.getAllCurrencies();
            } else {
                String currencyCode = pathInfo.substring(1); // удаляем начальный "/"
                System.out.println("Currency Code: " + currencyCode);
                result = currencies.getCurrency(currencyCode);
            }
        }
        catch (UniqueConstraintViolationException e) {
            System.out.println("Unique conflict " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }

        JSONSerializer json = new JSONSerializer();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String jsRes =  json.exclude("*.class").serialize(result);;
        out.println(jsRes);
        out.flush();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            CurrencyForm data = Parsing.getPostCurrency(request);
            CurrencyDTO addedData = currencies.add(data);
            JSONSerializer json = new JSONSerializer();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_CREATED);
            PrintWriter out = response.getWriter();
            String jsRes = json.exclude("*.class").serialize(addedData);
            out.println(jsRes);
            out.flush();
        } catch (UniqueConstraintViolationException e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }
    }
}