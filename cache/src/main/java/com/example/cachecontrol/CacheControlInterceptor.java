package com.example.cachecontrol;

import static org.springframework.http.HttpHeaders.CACHE_CONTROL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class CacheControlInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        response.addHeader(CACHE_CONTROL, "no-cache, private");
        return true;
    }
}
