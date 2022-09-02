package com.example.cachecontrol;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.List;

@Configuration
public class CacheWebConfig implements WebMvcConfigurer {

    private final List<HandlerInterceptor> handlerInterceptors;

    public CacheWebConfig(final List<HandlerInterceptor> handlerInterceptors) {
        this.handlerInterceptors = handlerInterceptors;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        handlerInterceptors.forEach(registry::addInterceptor);
    }
}
