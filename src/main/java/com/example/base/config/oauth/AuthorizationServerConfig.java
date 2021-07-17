package com.example.base.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.Resource;

/**
 * oauth2授权服务器配置类
 * 2020年9月11日 17:53:19
 * 尹泽鹏
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Resource
    private TokenStore tokenStore;

    @Resource
    private AuthenticationManager authenticationManager;

    /**
     * 客户端参数配置
     * 2020年9月11日 17:54:35
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //配置客户端的 service，也就是应用怎么获取到客户端的信息，一般来说是从内存或者数据库中获取，已经提供了他们的默认实现，你也可以自定义。
        clients.inMemory()
                //客户端id  标识客户的id
                .withClient("root")
                //配置密码 没有就不用配置
                .secret(new BCryptPasswordEncoder().encode("root"))
                //资源id
                .resourceIds("refuse_classification")
                //access_token过期时间（自定义TokenServices后此处失效）
                .accessTokenValiditySeconds(60 * 60 * 12)
                //refresh_token过期时间（自定义TokenServices后此处失效）
                .refreshTokenValiditySeconds(60 * 60 * 24 * 7)
                //此客户端可以使用的授权类型
                .authorizedGrantTypes("password", "refresh_token")
                //用来限制客户端访问范围,如果为空 则不限制
                .scopes("all");
        //.redirectUris("http://localhost/index.html");
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //认证管理器，当你选择了资源所有者密码(password)授权类型的时候，请设置 这个属性注入一个 AuthenticationManager 对象。
                .authenticationManager(authenticationManager)
                .tokenStore(tokenStore)
                //获取token请求方式
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
        //.pathMapping("/oauth/token","/getToken");  //修改获取token的url

    }

    /**
     * 自定义TokenServices
     */
    @Bean
    public AuthorizationServerTokenServices defaultTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore);
        tokenServices.setSupportRefreshToken(true);
        // token有效期自定义设置，默认12小时
        tokenServices.setAccessTokenValiditySeconds(60 * 60 * 12);
        // refresh_token默认7天
        tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 7);
        return tokenServices;
    }

    /**
     * 完成redis缓存  将令牌信息存储到redis缓存中
     */
    @Bean
    public TokenStore redisTokenStore(RedisConnectionFactory redisConnectionFactory) {
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * 配置跨域
     */
    /*@Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        CorsConfigurationSource source = request -> {
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.addAllowedHeader("*");
            corsConfiguration.addAllowedOrigin("*");
            corsConfiguration.addAllowedMethod("*");
            corsConfiguration.setAllowCredentials(true);
//            corsConfiguration.setMaxAge(3600L);
            return corsConfiguration;
        };

        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients()
                .addTokenEndpointAuthenticationFilter(new CorsFilter(source));
    }*/
}
