package com.sabomanq.currencyservice.http;

import com.sabomanq.currencyservice.dao.ExchangeRatesDAO;
import com.sabomanq.currencyservice.dao.SqliteProvider;
import com.sabomanq.currencyservice.model.Parsing;
import com.sabomanq.currencyservice.model.dto.Error;
import com.sabomanq.currencyservice.model.dto.ExchangeRateSumDTO;
import com.sabomanq.currencyservice.model.form.ExchangeTransactionForm;
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
            ExchangeRateSumDTO result = calculator.calculateExchangeSum(Parsing.getExchangeTransactionForm(request));
            response.setStatus(HttpServletResponse.SC_OK);
            Util.printToJs(result, response);
        } catch (NumberFormatException err) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Util.printToJs(new Error("Invalid amount format"), response);
        } catch (IllegalArgumentException err) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Util.printToJs(new Error("Invalid request"), response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Util.printToJs(new Error(e.getMessage()), response);
        }
    }

    @Override
    public void init() {
        this.calculator = new ExchangeCalculator(new ExchangeRatesDAO(new SqliteProvider()));
    }
}
