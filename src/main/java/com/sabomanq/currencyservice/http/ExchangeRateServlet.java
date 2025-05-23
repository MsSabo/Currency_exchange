package com.sabomanq.currencyservice.http;

import com.sabomanq.currencyservice.dao.DatabaseError;
import com.sabomanq.currencyservice.dao.ExchangeRatesDAO;
import com.sabomanq.currencyservice.dao.NotFoundException;
import com.sabomanq.currencyservice.dao.SqliteProvider;
import com.sabomanq.currencyservice.model.Parsing;
import com.sabomanq.currencyservice.model.dto.Error;
import com.sabomanq.currencyservice.model.form.ExchangeListForm;
import com.sabomanq.currencyservice.service.ExchangeRates;
import flexjson.JSONSerializer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(value = "/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private ExchangeRates rates;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        String pathInfo = req.getPathInfo(); // вернёт "/USD"
        String pair = pathInfo.substring(1);

        System.out.println("Pair: " + pair);
        if (pair.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            Util.printToJs(new Error("The currency pair codes are missing from the address."), response);
            return;
        }

        try {
            Object result = rates.getExchangeRate(pair);
            response.setStatus(HttpServletResponse.SC_OK);
            Util.printToJs(result, response);
        } catch (NotFoundException err) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            Util.printToJs(new Error(err.getMessage()), response);
        } catch (DatabaseError err) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Util.printToJs(new Error(err.getMessage()), response);
        }
    }

    public void doPatch(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doPatch");
        ExchangeListForm data = Parsing.getExchangePost(req);
        JSONSerializer json = new JSONSerializer();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String jsRes =  json.exclude("*.class").serialize(rates.updateExchangeRate(data));
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

    @Override
    public void init() {
        rates = new ExchangeRates(new ExchangeRatesDAO(new SqliteProvider()));
    }

}
