package com.example.cachecontrol;

import static org.springframework.http.HttpHeaders.CACHE_CONTROL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.HandlerInterceptor;

public class CacheInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        try {
            final String cacheControl = CacheControl
                    .noCache()
                    .cachePrivate()
                    .getHeaderValue();
            response.addHeader(CACHE_CONTROL, cacheControl);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }
}
