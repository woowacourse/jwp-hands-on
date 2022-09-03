package com.example.cachecontrol;

import com.google.common.net.HttpHeaders;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.HandlerInterceptor;

public class NoCacheInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
                             final Object handler) {
        response.addHeader(HttpHeaders.CACHE_CONTROL, CacheControl.noCache().cachePrivate().getHeaderValue());
        return true;
    }
}
