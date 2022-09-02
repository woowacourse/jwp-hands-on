package com.example.cachecontrol;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class CacheInterceptor implements HandlerInterceptor {
    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
                           final ModelAndView modelAndView) {
        response.addHeader("Cache-Control", "no-cache, private");
    }
}
