package com.sabomanq.currencyservice.http;

import com.sabomanq.currencyservice.dao.ExchangeRatesDAO;
import com.sabomanq.currencyservice.dao.SqliteProvider;
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
            String fromCode = request.getParameter("from");
            String toCode = request.getParameter("to");
            Float amount = Float.parseFloat(request.getParameter("amount"));

            ExchangeRateSumDTO result = calculator.calculateExchangeSum(new ExchangeTransactionForm(fromCode, toCode, amount));
            response.setStatus(HttpServletResponse.SC_OK);
            Util.printToJs(result, response);
        } catch (NumberFormatException err) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Util.printToJs(new Error("Invalid amount format"), response);
        } catch (Exception e) {
            //e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Util.printToJs(new Error(e.getMessage()), response);
        } // @todo catch errors
    }

    @Override
    public void init() {
        this.calculator = new ExchangeCalculator(new ExchangeRatesDAO(new SqliteProvider()));
    }
}
