package com.example.base.config.oauth;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Options预请求过滤器
 */
//@Configuration
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsConfig implements Filter {
    /**
     * 过滤预请求方法
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 允许所有的域访问
        response.setHeader("Access-Control-Allow-Origin", "*");
        // 允许所有方式的请求
        response.setHeader("Access-Control-Allow-Methods", "*");
        // 是否可以允许发送cookie
        response.setHeader("Access-Control-Allow-Credentials", "true");
        // 头信息缓存有效时长（如果不设 Chromium 同时规定了一个默认值 5 秒），没有缓存将已OPTIONS进行预请求
        response.setHeader("Access-Control-Max-Age", "86400");
        // 允许的头信息
        response.setHeader("Access-Control-Allow-Headers", "*");

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}

