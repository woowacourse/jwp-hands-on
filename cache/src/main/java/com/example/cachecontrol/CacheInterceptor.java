package com.example.cachecontrol;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CacheInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception ex) throws Exception {
        if (!response.containsHeader(HttpHeaders.CACHE_CONTROL)) {
            final CacheControl cacheControl = CacheControl.noCache()
                    .cachePrivate();
            response.setHeader(HttpHeaders.CACHE_CONTROL, cacheControl.getHeaderValue());
        }
    }
}
