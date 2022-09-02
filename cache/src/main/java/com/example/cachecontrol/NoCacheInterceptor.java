package com.example.cachecontrol;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

public class NoCacheInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
                             final Object handler) {
        CacheControl cacheControl = CacheControl.noCache().cachePrivate();
        response.addHeader(HttpHeaders.CACHE_CONTROL, cacheControl.getHeaderValue());
        return true;
    }
}
