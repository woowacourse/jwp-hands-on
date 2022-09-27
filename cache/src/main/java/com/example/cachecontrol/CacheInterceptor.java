package com.example.cachecontrol;

import com.google.common.net.HttpHeaders;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class CacheInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
                           final ModelAndView modelAndView) throws Exception {
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, private");
    }
}
