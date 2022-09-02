package com.example.etag;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import static com.example.version.CacheBustingWebConfig.PREFIX_STATIC_RESOURCES;

@Configuration
public class EtagFilterConfiguration {

    @Bean
    public FilterRegistrationBean<ShallowEtagHeaderFilter> shallowEtagHeaderFilter() {
        final FilterRegistrationBean<ShallowEtagHeaderFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new ShallowEtagHeaderFilter());
        registration.addUrlPatterns(
                "/etag",

                // js, css 같은 정적 파일에 ETag를 적용하기 위해 경로 추가
                PREFIX_STATIC_RESOURCES + "/*"
        );
        return registration;
    }
}
