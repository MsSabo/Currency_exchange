package com.sabomanq.currencyservice.http;

import flexjson.JSONSerializer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Util {
    public static void printToJs(Object obj, HttpServletResponse response) throws IOException {
        JSONSerializer json = new JSONSerializer();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String jsRes =  json.exclude("*.class").serialize(obj);;
        out.println(jsRes);
        out.flush();
    }
}
