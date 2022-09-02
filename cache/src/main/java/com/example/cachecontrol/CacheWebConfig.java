package com.example.cachecontrol;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CacheWebConfig implements WebMvcConfigurer {

    private final HandlerInterceptor cacheInterceptor;

    public CacheWebConfig(final HandlerInterceptor cacheInterceptor) {
        this.cacheInterceptor = cacheInterceptor;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(cacheInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/resources/**");
    }
}
