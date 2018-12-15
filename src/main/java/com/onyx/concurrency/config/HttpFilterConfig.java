package com.onyx.concurrency.config;

import com.onyx.concurrency.example.threadlocal.HttpFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpFilterConfig {

    /**
     * Filter的注册
     * @return
     */
    @Bean
    public FilterRegistrationBean httpFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new HttpFilter());
        bean.addUrlPatterns("/threadLocal/*");
        return bean;
    }
}
