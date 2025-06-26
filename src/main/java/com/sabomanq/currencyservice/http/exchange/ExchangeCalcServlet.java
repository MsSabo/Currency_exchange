package com.sabomanq.currencyservice.http.exchange;

import com.sabomanq.currencyservice.dao.CurrencyDb;
import com.sabomanq.currencyservice.dao.ExchangeRatesDAO;
import com.sabomanq.currencyservice.dao.NotFoundException;
import com.sabomanq.currencyservice.dao.SqliteProvider;
import com.sabomanq.currencyservice.http.Util;
import com.sabomanq.currencyservice.http.RequestParser;
import com.sabomanq.currencyservice.model.dto.ErrorDTO;
import com.sabomanq.currencyservice.model.dto.ExchangeRateSumDTO;
import com.sabomanq.currencyservice.service.ExchangeCalculator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns="/exchange")
public class ExchangeCalcServlet extends HttpServlet {
    private ExchangeCalculator calculator;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            ExchangeRateSumDTO result = calculator.conversionRate(RequestParser.getExchangeTransactionForm(request));
            response.setStatus(HttpServletResponse.SC_OK);
            Util.printToJs(result, response);
        } catch (NumberFormatException err) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Util.printToJs(new ErrorDTO("Invalid amount format"), response);
        } catch (RuntimeException err) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Util.printToJs(new ErrorDTO(err.getMessage()), response);
        } catch (NotFoundException err) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            Util.printToJs(new ErrorDTO(err.getMessage()), response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Util.printToJs(new ErrorDTO(e.getMessage()), response);
        }
    }

    @Override
    public void init() {
        this.calculator = new ExchangeCalculator(new ExchangeRatesDAO(new SqliteProvider()), new CurrencyDb(new SqliteProvider()));
    }
}
