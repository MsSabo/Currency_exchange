package com.sabomanq.currencyservice.http;

import com.sabomanq.currencyservice.dao.ExchangeRatesDAO;
import com.sabomanq.currencyservice.dao.SqliteProvider;
import com.sabomanq.currencyservice.model.form.ExchangeListForm;
import com.sabomanq.currencyservice.model.Parsing;
import com.sabomanq.currencyservice.service.ExchangeRates;
import flexjson.JSONSerializer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(value = "/exchangeRates/*")
public class ExchangeServlet extends HttpServlet {
    private ExchangeRates exchangeRate;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        String pathInfo = req.getPathInfo(); // вернёт "/USD"

        Object result;
        if (pathInfo == null || pathInfo.equals("/")) {
            result = exchangeRate.getExchangeRates();
        } else {
            String pair = pathInfo.substring(1);
            System.out.println("Pair: " + pair);
            result = exchangeRate.getExchangeRate(pair);
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
    public void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException {
        ExchangeListForm data = Parsing.getExchangePost(req);
        JSONSerializer json = new JSONSerializer();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String jsRes =  json.exclude("*.class").serialize(exchangeRate.addExchangeRate(data));
        out.println(jsRes);
        out.flush();
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("PATCH".equals(req.getMethod())) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    public void doPatch(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doPatch");
        ExchangeListForm data = Parsing.getExchangePost(req);
        JSONSerializer json = new JSONSerializer();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String jsRes =  json.exclude("*.class").serialize(exchangeRate.updateExchangeRate(data));
        out.println(jsRes);
        out.flush();
    }

    @Override
    public void init() {
        exchangeRate = new ExchangeRates(new ExchangeRatesDAO(new SqliteProvider()));
    }
}
