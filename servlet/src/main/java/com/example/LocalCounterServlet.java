package com.example;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "localCounterServlet", urlPatterns = "/local-counter")
public class LocalCounterServlet extends HttpServlet {

    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
        getServletContext().log("init() 호출");
    }

    /**
     * localCounter 같은 로컬 변수는 다른 스레드와 공유되지 않는다.
     * 비즈니스 로직 처리는 로컬 변수를 사용한다.
     */
    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        getServletContext().log("service() 호출");
        response.addHeader("Content-Type", "text/html; charset=utf-8");
        int localCounter = 0;
        localCounter++;
        response.getWriter().write(String.valueOf(localCounter));
    }

    @Override
    public void destroy() {
        getServletContext().log("destroy() 호출");
    }
}
