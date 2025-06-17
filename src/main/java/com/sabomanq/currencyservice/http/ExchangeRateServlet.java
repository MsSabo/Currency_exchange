package com.sabomanq.currencyservice.http;

import com.sabomanq.currencyservice.dao.DatabaseError;
import com.sabomanq.currencyservice.dao.ExchangeRatesDAO;
import com.sabomanq.currencyservice.dao.NotFoundException;
import com.sabomanq.currencyservice.dao.SqliteProvider;
import com.sabomanq.currencyservice.model.Parsing;
import com.sabomanq.currencyservice.model.dto.Error;
import com.sabomanq.currencyservice.model.dto.ExchangeRateDTO;
import com.sabomanq.currencyservice.model.entity.ExchangeRateFull;
import com.sabomanq.currencyservice.model.form.ExchangeRateForm;
import com.sabomanq.currencyservice.model.mapper.ExchangeRateMapper;
import com.sabomanq.currencyservice.service.ExchangeRates;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(value = "/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private ExchangeRates rates;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        String pathInfo = req.getPathInfo(); // вернёт "/USD"
        String pair = pathInfo.substring(1);

        if (pair.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            Util.printToJs(new Error("The currency pair codes are missing from the address."), response);
            return;
        }

        try {
            Optional<ExchangeRateFull> result = rates.getExchangeRate(pair);
            if (!result.isPresent()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                Util.printToJs(new Error("Exchange pair not found."), response);
                return;
            }
            ExchangeRateDTO output = ExchangeRateMapper.INSTANCE.toDto(result.get());
            response.setStatus(HttpServletResponse.SC_OK);
            Util.printToJs(output, response);
        } catch (DatabaseError err) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Util.printToJs(new Error(err.getMessage()), response);
        }
    }

    public void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String pathInfo = request.getPathInfo(); // вернёт "/USD"
            String exchangePair = pathInfo.substring(1); // удаляем начальный "/"
            ExchangeRateForm form = Parsing.getExchangeRateForm(request);

            ExchangeRateDTO result = ExchangeRateMapper.INSTANCE.toDto(rates.updateExchangeRate(exchangePair, form));
            response.setStatus(HttpServletResponse.SC_OK);
            Util.printToJs(result, response);
        } catch (NotFoundException err) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            Util.printToJs(new Error(err.getMessage()), response);
        } catch (DatabaseError err) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Util.printToJs(new Error(err.getMessage()), response);
        } catch (IllegalArgumentException err) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Util.printToJs(new Error(err.getMessage()), response);
        }
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
