package com.example.cachecontrol;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class CacheControlInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) {
        final var headers = response.getHeaderNames();
        if (headers.contains(HttpHeaders.CACHE_CONTROL)) {
            return;
        }
        response.addHeader(HttpHeaders.CACHE_CONTROL, defaultCacheControl());
    }

    private String defaultCacheControl() {
        return CacheControl.noCache()
                .cachePrivate()
                .getHeaderValue();
    }
}
