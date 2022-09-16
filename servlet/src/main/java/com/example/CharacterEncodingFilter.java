package com.example;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;
import org.apache.tomcat.util.buf.Utf8Encoder;

@WebFilter("/*")
public class CharacterEncodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.getServletContext().log("doFilter() 호출");

        String characterEncoding = response.getCharacterEncoding();
        response.setCharacterEncoding("UTF-8");
        chain.doFilter(request, response);
    }
}
