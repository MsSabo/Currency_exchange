package com.sabomanq.currencyservice.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class BasicFilter implements Filter {

    public void init(FilterConfig filterConfig) {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        res.setHeader("Access-Control-Allow-Origin", "https://72a8-185-53-231-224.ngrok-free.app"); // или указать конкретный origin
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        res.setHeader("Access-Control-Max-Age", "3600");
        chain.doFilter(request, response);
        int status = res.getStatus(); // Получаем статус после обработки
        System.out.println("HTTP status: " + status);
    }

    public void destroy() {
    }
}
