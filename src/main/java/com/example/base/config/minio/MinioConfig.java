package com.example.base.config.minio;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class MinioConfig {

    @Resource
    private MinioProperties clientConfig;

    @Bean
    public MinioClient getMinioClient() throws InvalidPortException, InvalidEndpointException {
        return new MinioClient(
                clientConfig.getUrl(),
                clientConfig.getAccessKey(),
                clientConfig.getSecretKey()
        );
    }
}
