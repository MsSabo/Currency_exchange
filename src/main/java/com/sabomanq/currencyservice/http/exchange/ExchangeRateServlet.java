package com.sabomanq.currencyservice.http.exchange;

import com.sabomanq.currencyservice.dao.DatabaseError;
import com.sabomanq.currencyservice.dao.ExchangeRatesDAO;
import com.sabomanq.currencyservice.dao.NotFoundException;
import com.sabomanq.currencyservice.dao.SqliteProvider;
import com.sabomanq.currencyservice.http.BadRequest;
import com.sabomanq.currencyservice.http.Util;
import com.sabomanq.currencyservice.http.RequestParser;
import com.sabomanq.currencyservice.model.dto.ErrorDTO;
import com.sabomanq.currencyservice.model.dto.ExchangeRateDTO;
import com.sabomanq.currencyservice.model.entity.ExchangeRate;
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
            Util.printToJs(new ErrorDTO("The currency pair codes are missing from the address."), response);
            return;
        }

        try {
            Optional<ExchangeRate> rate = rates.getExchangeRate(pair);
            if (rate.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                Util.printToJs(new ErrorDTO("Exchange pair not found."), response);
                return;
            }

            ExchangeRateDTO output = ExchangeRateMapper.INSTANCE.toDto(rate.get());
            response.setStatus(HttpServletResponse.SC_OK);
            Util.printToJs(output, response);
        } catch (DatabaseError err) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Util.printToJs(new ErrorDTO(err.getMessage()), response);
        }
    }

    public void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String pathInfo = request.getPathInfo();
            String exchangePair = pathInfo.substring(1);
            ExchangeRateForm form = RequestParser.getExchangeRateForm(request);

            ExchangeRateDTO updatedRate = ExchangeRateMapper.INSTANCE.toDto(rates.updateExchangeRate(exchangePair, form));
            response.setStatus(HttpServletResponse.SC_OK);
            Util.printToJs(updatedRate, response);
        } catch (NotFoundException err) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            Util.printToJs(new ErrorDTO(err.getMessage()), response);
        } catch (DatabaseError err) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Util.printToJs(new ErrorDTO(err.getMessage()), response);
        } catch (BadRequest err) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Util.printToJs(new ErrorDTO(err.getMessage()), response);
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
