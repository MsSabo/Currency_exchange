package com.sabomanq.currencyservice.http;

import com.sabomanq.currencyservice.dao.CurrencyDb;
import com.sabomanq.currencyservice.dao.DatabaseError;
import com.sabomanq.currencyservice.dao.NotFoundException;
import com.sabomanq.currencyservice.dao.SqliteProvider;
import com.sabomanq.currencyservice.model.dto.CurrencyDTO;
import com.sabomanq.currencyservice.model.dto.Error;
import com.sabomanq.currencyservice.service.Currencies;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(value = "/currency/*")
public class CurrencyServlet extends HttpServlet {
    private Currencies currencies;

    @Override
    public void init() {
        currencies = new Currencies(new CurrencyDb(new SqliteProvider()));
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo(); // вернёт "/USD"
        try {
            String currencyCode = pathInfo.substring(1); // удаляем начальный "/"
            System.out.println("Currency Code: " + currencyCode);
            if (currencyCode.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                Util.printToJs(new Error("Currency code is empty"), response);
                return;
            }

            CurrencyDTO result = currencies.getCurrency(currencyCode);
            response.setStatus(HttpServletResponse.SC_OK);
            Util.printToJs(result, response);
        } catch (NotFoundException err) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            Util.printToJs(new Error("Currency not found"), response);
            System.out.println("Currency not found printed to response");
        } catch (DatabaseError err) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Util.printToJs(new Error("Internal error"), response);
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            System.out.println(e.getMessage());
            Util.printToJs(new Error("Internal error"), response);
        }
    }

}
