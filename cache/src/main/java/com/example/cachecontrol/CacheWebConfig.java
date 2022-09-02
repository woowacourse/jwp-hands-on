package com.example.cachecontrol;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CacheWebConfig implements WebMvcConfigurer {

    private final CacheControllInterceptor cacheControllInterceptor;

    public CacheWebConfig(CacheControllInterceptor cacheControllInterceptor) {
        this.cacheControllInterceptor = cacheControllInterceptor;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(cacheControllInterceptor)
                .addPathPatterns("/**");
    }
}
