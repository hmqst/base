package com.example.base.config.oauth;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.annotation.Resource;

/**
 * 资源服务器配置类
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    /**
     * token过期支持
     */
    @Resource
    private TokenStore tokenStore;
    @Resource
    private AuthExceptionEntryPoint authExceptionEntryPoint;
    @Resource
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        super.configure(resources);
        // 配置资源id
        resources.resourceId("refuse_classification")
                .tokenStore(tokenStore)
                // 自定义token过期返回
                .authenticationEntryPoint(authExceptionEntryPoint)
                // 自定义无权限返回
                .accessDeniedHandler(customAccessDeniedHandler);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin/**").hasRole("admin")
                .antMatchers("/agent/**").hasAnyRole("agent", "admin")
                .antMatchers("/domain/**").hasAnyRole("domain", "agent", "admin")
                .antMatchers("/device/**").hasRole("device")
                .anyRequest().authenticated();
    }
}
