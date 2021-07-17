package com.example.base.config.oauth;

import com.example.base.utils.ResultBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @date 2020年11月18日09:37:58
 * @notes 重写自定义无权限返回
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        try {
            objectMapper.writeValue(response.getOutputStream(),
                    ResultBean.fail(
                            HttpServletResponse.SC_FORBIDDEN,
                            "您无权访问此资源：" +
                            accessDeniedException.getMessage()
                    )
            );
        } catch (Exception e) {
            throw new ServletException();
        }
    }
}
