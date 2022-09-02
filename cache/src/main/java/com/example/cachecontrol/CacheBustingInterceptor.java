package com.example.cachecontrol;

import java.time.Duration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class CacheBustingInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
                           final ModelAndView modelAndView) throws Exception {
        final String cacheControl = CacheControl
                .maxAge(Duration.ofDays(365))
                .cachePublic()
                .getHeaderValue();

        response.setHeader(HttpHeaders.CACHE_CONTROL, cacheControl);
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
