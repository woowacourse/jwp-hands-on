package com.example.etag;

import static com.example.version.CacheBustingWebConfig.PREFIX_STATIC_RESOURCES;

import com.example.version.ResourceVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@Configuration
public class EtagFilterConfiguration {

    @Autowired
    private ResourceVersion version;

    @Bean
    public FilterRegistrationBean<ShallowEtagHeaderFilter> shallowEtagHeaderFilter() {
        FilterRegistrationBean<ShallowEtagHeaderFilter> filterRegistrationBean = new FilterRegistrationBean<>(
                new ShallowEtagHeaderFilter());
        filterRegistrationBean
                .addUrlPatterns("/etag", PREFIX_STATIC_RESOURCES + "/*");
        return filterRegistrationBean;
    }
}
