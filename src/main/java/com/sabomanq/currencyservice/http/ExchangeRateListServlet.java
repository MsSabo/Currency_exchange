package com.sabomanq.currencyservice.http;

import com.sabomanq.currencyservice.dao.DatabaseError;
import com.sabomanq.currencyservice.dao.ExchangeRatesDAO;
import com.sabomanq.currencyservice.dao.SqliteProvider;
import com.sabomanq.currencyservice.dao.UniqueConstraintViolationException;
import com.sabomanq.currencyservice.model.dto.Error;
import com.sabomanq.currencyservice.model.dto.ExchangeRateDTO;
import com.sabomanq.currencyservice.model.entity.ExchangeRate;
import com.sabomanq.currencyservice.model.form.ExchangeListForm;
import com.sabomanq.currencyservice.model.Parsing;
import com.sabomanq.currencyservice.service.ExchangeRates;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
            ExchangeRate result = exchangeRate.addExchangeRate(data);
            //ExchangeRateDTO output = new ExchangeRateDTO(result.id, result.baseCurrency, result.targetCurrency, result.rate);
            Util.printToJs(result, response);
        } catch (UniqueConstraintViolationException err) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            Util.printToJs(new Error(err.getMessage()), response);
        } catch (IllegalArgumentException err) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Util.printToJs(new Error(err.getMessage()), response);
        } catch (DatabaseError err) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Util.printToJs(new Error(err.getMessage()), response);
        }
    }

    @Override
    public void init() {
        exchangeRate = new ExchangeRates(new ExchangeRatesDAO(new SqliteProvider()));
    }
}
