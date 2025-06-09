package com.sabomanq.currencyservice.http;



import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Util {
    public static void printToJs(Object obj, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        String jsRes = mapper.writeValueAsString(obj);
        out.println(jsRes);
        out.flush();
    }
}
