/*
package com.example.base.config.oauth;

import com.example.base.service.impl.UserDetailsServiceImpl;
import com.example.base.utils.ResultBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

*/
/**
 * 安全控制配置类
 *//*

@Configuration
@EnableWebSecurity
public class SecurityConfig1 extends WebSecurityConfigurerAdapter {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    */
/**
     * 定义密码加密方式
     *//*

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    */
/**
     * 自定义用户及其角色 (基于数据库定义)
     *//*

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    */
/**
     * 配置绕过security验证  不走Spring security   把前台所有路径都放开
     *//*

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring().antMatchers(
                "/api/**",
                "/swagger-ui.html",
                "/swagger-ui/*",
                "/swagger-resources/**",
                "/v2/api-docs",
                "/webjars/**"
        );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 路径安全
        http.authorizeRequests()
                .antMatchers("/admin/**").hasRole("admin")
                .antMatchers("/user/**").hasAnyRole("user", "admin")
                .anyRequest().authenticated().and()
                .cors().and()
                .csrf().disable();
        // 定义登录处理器
        http.formLogin().loginProcessingUrl("/login").usernameParameter("username").passwordParameter("password").successHandler((req, resp, auth) -> {
            Object principal = auth.getPrincipal();
            resp.setContentType("application/json;charset=utf-8");
            PrintWriter out = resp.getWriter();
            resp.setStatus(200);
            out.write(objectMapper.writeValueAsString(ResultBean.success(principal)));
            out.flush();
            out.close();
        }).failureHandler((req, resp, e) -> {
            resp.setContentType("application/json;charset=utf-8");
            PrintWriter out = resp.getWriter();
            resp.setStatus(200);
            String description;
            if (e instanceof LockedException) {
                description = "账户被锁定";
            } else if (e instanceof DisabledException) {
                description = "账户被禁用";
            } else if (e instanceof AccountExpiredException) {
                description = "账户已过期";
            } else if (e instanceof CredentialsExpiredException) {
                description = "密码已过期";
            } else if (e instanceof BadCredentialsException) {
                description = "账户名或密码错误";
            } else {
                description = "登录失败";
            }
            out.write(objectMapper.writeValueAsString(ResultBean.fail(description)));
            out.flush();
            out.close();
        }).permitAll();

        // 自定义退出处理器
        http.logout().logoutUrl("/logout").clearAuthentication(true).invalidateHttpSession(true).logoutSuccessHandler(new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication auth) throws IOException, ServletException {
                resp.setContentType("application/json;charset=utf-8");
                PrintWriter out = resp.getWriter();
                resp.setStatus(200);
                out.write(objectMapper.writeValueAsString(ResultBean.success()));
                out.flush();
                out.close();
            }
        }).permitAll();
        // 自定义异常处理器
        http.exceptionHandling() // 异常处理器
                // 用来解决匿名用户访问无权限资源时的异常
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                        response.setContentType("application/json;charset=utf-8");
                        PrintWriter out = response.getWriter();
                        response.setStatus(200);
                        out.write(objectMapper.writeValueAsString(ResultBean.fail(401, "未登录或登录状态已过期")));
                        out.flush();
                        out.close();
                    }
                })
                // 用来解决认证过的用户访问无权限资源时的异常
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        response.setContentType("application/json;charset=utf-8");
                        PrintWriter out = response.getWriter();
                        response.setStatus(200);
                        out.write(objectMapper.writeValueAsString(ResultBean.fail("权限不足")));
                        out.flush();
                        out.close();
                    }
                });
        // session管理
        http.sessionManagement().sessionFixation().changeSessionId()
                // 最大会话数
                .maximumSessions(1)
                // 达到最大数后, 强制验证, 踢出旧用户
                .maxSessionsPreventsLogin(false)
                // 旧用户被踢出后, 执行的内容
                .expiredSessionStrategy(new SessionInformationExpiredStrategy() {
                    @Override
                    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
                        HttpServletResponse response = event.getResponse();
                        response.setContentType("application/json;charset=utf-8");
                        PrintWriter out = response.getWriter();
                        response.setStatus(200);
                        out.write(objectMapper.writeValueAsString(ResultBean.fail(401,"您的登录状态已过期")));
                        out.flush();
                        out.close();
                    }
                });
    }
}
*/
