package com.example.base.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author benben
 * @date 2021-05-28 9:33
 */
@Configuration
public class ESClientConfig {

    @Bean
    RestHighLevelClient restHighLevelClient(){
        return new RestHighLevelClient(RestClient.builder(
                new HttpHost("127.0.0.1", 9200, "http")
        ));
    }
}
