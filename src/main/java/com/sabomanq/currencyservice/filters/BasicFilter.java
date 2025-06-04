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

        chain.doFilter(request, response);
        int status = res.getStatus(); // Получаем статус после обработки
        System.out.println("HTTP status: " + status);
    }

    public void destroy() {
    }
}
