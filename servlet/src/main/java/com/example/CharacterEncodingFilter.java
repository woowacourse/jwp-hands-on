package com.example;

import static org.eclipse.jdt.internal.compiler.util.Util.UTF_8;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@WebFilter("/*")
public class CharacterEncodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.getServletContext().log("doFilter() 호출");
        response.setCharacterEncoding(UTF_8);
        chain.doFilter(request, response);
    }
}
