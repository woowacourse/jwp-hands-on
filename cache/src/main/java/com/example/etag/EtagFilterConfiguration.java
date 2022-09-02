package com.example.etag;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@Configuration
public class EtagFilterConfiguration {

    @Bean
    public FilterRegistrationBean<ShallowEtagHeaderFilter> shallowEtagHeaderFilter() {
        final FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new ShallowEtagHeaderFilter());
        filterRegistrationBean.addUrlPatterns("/etag", "/resources/*");
        return filterRegistrationBean;
    }
}
