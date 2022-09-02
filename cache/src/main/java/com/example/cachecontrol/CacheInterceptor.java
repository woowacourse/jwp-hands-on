package com.example.cachecontrol;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class CacheInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
                           final ModelAndView modelAndView) {
        final String requestURI = request.getRequestURI();
        final String cacheControlValue = CacheControl.noCache()
                .cachePrivate()
                .getHeaderValue();
        response.setHeader(HttpHeaders.CACHE_CONTROL, cacheControlValue);
    }
}
