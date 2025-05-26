package com.sabomanq.currencyservice.http;

import com.sabomanq.currencyservice.dao.ExchangeRatesDAO;
import com.sabomanq.currencyservice.dao.SqliteProvider;
import com.sabomanq.currencyservice.dao.UniqueConstraintViolationException;
import com.sabomanq.currencyservice.model.dto.Error;
import com.sabomanq.currencyservice.model.entity.ExchangeRateInfo;
import com.sabomanq.currencyservice.model.form.ExchangeListForm;
import com.sabomanq.currencyservice.model.Parsing;
import com.sabomanq.currencyservice.service.ExchangeRates;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(value = "/exchangeRates/*")
public class ExchangeRateListServlet extends HttpServlet {
    private ExchangeRates exchangeRate;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        try {
            Object result = exchangeRate.getExchangeRates();
            Util.printToJs(result, response);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception err) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Util.printToJs(new Error(err.getMessage()), response);
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException {
        try {
            ExchangeListForm data = Parsing.getExchangePost(req);
            Optional<ExchangeRateInfo> result = exchangeRate.addExchangeRate(data);
            Util.printToJs(result.get(), response);
        } catch (UniqueConstraintViolationException err) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            Util.printToJs(new Error(err.getMessage()), response);
        } catch (IllegalArgumentException err) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Util.printToJs(new Error(err.getMessage()), response);
        } catch (Exception err) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Util.printToJs(new Error(err.getMessage()), response);
        }
    }

    @Override
    public void init() {
        exchangeRate = new ExchangeRates(new ExchangeRatesDAO(new SqliteProvider()));
    }
}
