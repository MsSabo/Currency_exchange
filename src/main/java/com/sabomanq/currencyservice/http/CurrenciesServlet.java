package com.sabomanq.currencyservice.http;

import com.sabomanq.currencyservice.dao.*;
import com.sabomanq.currencyservice.model.dto.CurrencyDTO;
import com.sabomanq.currencyservice.model.dto.Error;
import com.sabomanq.currencyservice.model.form.CurrencyForm;
import com.sabomanq.currencyservice.model.Parsing;
import com.sabomanq.currencyservice.service.Currencies;
import flexjson.JSONSerializer;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import javax.xml.crypto.Data;

@WebServlet(value = "/currencies/*")
public class CurrenciesServlet extends HttpServlet {
    private Currencies currencies;

    @Override
    public void init() {
        currencies = new Currencies(new CurrencyDb(new SqliteProvider()));
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object result;
        try {
            result = currencies.getAllCurrencies();
            response.setStatus(HttpServletResponse.SC_OK);
            Util.printToJs(result, response);
        } catch (DatabaseError e) {
            System.out.println("Unique conflict " + e.getMessage() + " " + e.getCause());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Util.printToJs(new Error("Internal error"), response);
        } catch (IOException err) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Util.printToJs(new Error("Internal error"), response);
        }

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            CurrencyForm data = Parsing.getPostCurrency(request);
            CurrencyDTO addedData = currencies.add(data);
            response.setStatus(HttpServletResponse.SC_CREATED);
            Util.printToJs(addedData, response);
        } catch (UniqueConstraintViolationException e) {
            System.out.println("Unique conflict " + e.getMessage() + " " + e.getCause());
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            Util.printToJs(new Error("Currency already exists."), response);
        } catch (DatabaseError err) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Util.printToJs(new Error("Internal server error"), response);
        } catch (IllegalArgumentException err) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Util.printToJs(new Error(err.getMessage()), response);
        }
    }
}