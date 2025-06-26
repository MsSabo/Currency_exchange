package com.sabomanq.currencyservice.http.exchange;

import com.sabomanq.currencyservice.dao.DatabaseError;
import com.sabomanq.currencyservice.dao.ExchangeRatesDAO;
import com.sabomanq.currencyservice.dao.SqliteProvider;
import com.sabomanq.currencyservice.dao.UniqueConstraintViolationException;
import com.sabomanq.currencyservice.http.BadRequest;
import com.sabomanq.currencyservice.http.Util;
import com.sabomanq.currencyservice.model.dto.ErrorDTO;
import com.sabomanq.currencyservice.model.dto.ExchangeRateDTO;
import com.sabomanq.currencyservice.model.entity.ExchangeRate;
import com.sabomanq.currencyservice.model.form.ExchangeListForm;
import com.sabomanq.currencyservice.http.RequestParser;
import com.sabomanq.currencyservice.model.mapper.ExchangeRateMapper;
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
            Util.printToJs(new ErrorDTO(err.getMessage()), response);
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException {
        try {
            ExchangeListForm data = RequestParser.getExchangePost(req);
            ExchangeRate newRate = exchangeRate.addExchangeRate(data);
            ExchangeRateDTO output = ExchangeRateMapper.INSTANCE.toDto(newRate);
            Util.printToJs(output, response);
        } catch (BadRequest err) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Util.printToJs(new ErrorDTO(err.getMessage()), response);
        } catch (UniqueConstraintViolationException err) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            Util.printToJs(new ErrorDTO(err.getMessage()), response);
        }  catch (DatabaseError err) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Util.printToJs(new ErrorDTO(err.getMessage()), response);
        }
    }

    @Override
    public void init() {
        exchangeRate = new ExchangeRates(new ExchangeRatesDAO(new SqliteProvider()));
    }
}
