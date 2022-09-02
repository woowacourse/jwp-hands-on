package com.example.etag;

import java.util.Collections;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@Configuration
public class EtagFilterConfiguration {

    @Bean
    public FilterRegistrationBean<ShallowEtagHeaderFilter> shallowEtagHeaderFilter() {
        FilterRegistrationBean<ShallowEtagHeaderFilter> filterRegistrationBean = new FilterRegistrationBean(new ShallowEtagHeaderFilter());
        filterRegistrationBean.addUrlPatterns("/etag", "/resources/*"); // string 여러개를 가변인자로 받는 메소드

        return filterRegistrationBean;
    }
}
