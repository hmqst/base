package com.example.base.config.oauth;

import com.example.base.utils.ResultBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * token无效异常类重写
 * @date 2020年11月6日08:55:56
 */
@Component
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        try {
            objectMapper.writeValue(response.getOutputStream(),
                    ResultBean.fail(
                            HttpServletResponse.SC_UNAUTHORIZED,
                            "暂未登录或token已经过期：" +
                                    authException.getMessage())
            );
        } catch (Exception e) {
            throw new ServletException();
        }
    }
}
