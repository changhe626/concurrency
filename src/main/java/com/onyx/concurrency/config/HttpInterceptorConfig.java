package com.onyx.concurrency.config;

import com.onyx.concurrency.example.threadlocal.HttpInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class HttpInterceptorConfig extends WebMvcConfigurerAdapter {

    /**
     * Interceptor的注册
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截所有
        registry.addInterceptor(new HttpInterceptor()).addPathPatterns("/**");
    }
}
