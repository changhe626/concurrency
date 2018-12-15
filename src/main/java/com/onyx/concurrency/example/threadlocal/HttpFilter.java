package com.onyx.concurrency.example.threadlocal;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 过滤器
 */
public class HttpFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req=(HttpServletRequest)request;
        System.out.println("do filter:"+Thread.currentThread().getId()+",路径是:"+req.getServletPath());
        RequestHolder.add(Thread.currentThread().getId());
        chain.doFilter(request,response);

    }

    @Override
    public void destroy() {
        System.out.println("destory");
    }
}
