package com.example.base.config;

import org.springframework.http.HttpHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private static final String VERSION = "1.0.0";

    /**
     * 创建API的基本信息（这些基本信息会展示在文档页面中）
     * 访问地址： http://项目实际地址/swagger-ui.html
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //.useDefaultResponseMessages(false) // 通过 Docket 的 useDefaultResponseMessages 方法告诉 Swagger 不使用默认的 HTTP 响应消息
                .pathMapping("/")
                .select()
                //.apis(RequestHandlerSelectors.withClassAnnotation(Api.class)) // 只生成被Api这个注解注解过的类接
                .apis(RequestHandlerSelectors.basePackage("com.example.base.controller")) // 映射下的包所有接口都产生文档
                .paths(PathSelectors.any()) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build()
                .securitySchemes(Collections.singletonList(securitySchemes()))
                .securityContexts(Collections.singletonList(securityContexts()));
    }

    /**
     * 添加摘要信息
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .contact(new Contact("benben", null, "173844641@qq.com"))
                .title("account-server接口文档")
                .description("account-server接口文档")
                .termsOfServiceUrl("http://javadaily.cn")
                .license("许可证链接")
                .licenseUrl("许可证地址")
                .version(VERSION)
                .build();
    }

    private SecurityScheme apiKey() {
        return new ApiKey(HttpHeaders.AUTHORIZATION, HttpHeaders.AUTHORIZATION, "header");
    }

    /**
     * 认证方式使用密码模式
     */
    private OAuth securitySchemes() {
        GrantType grantType = new ResourceOwnerPasswordCredentialsGrant("/oauth/token");
        return new OAuthBuilder()
                // oauth2不可修改 否则不生效
                .name("oauth2")
                .grantTypes(Collections.singletonList(grantType))
                .scopes(Arrays.asList(scopes()))
                .build();
    }

    /**
     * 允许认证的scope
     */
    private AuthorizationScope[] scopes() {
        AuthorizationScope authorizationScope = new AuthorizationScope("all", "access_token");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return authorizationScopes;
    }

    /**
     * 设置 swagger2 认证的安全上下文
     */
    private SecurityContext securityContexts() {
        return SecurityContext.builder()
                .securityReferences(
                        // Bearer
                        Collections.singletonList(new SecurityReference("Bearer", scopes()))
                )
                .forPaths(PathSelectors.any())
                .build();
    }
}
