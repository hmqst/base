package com.example.base.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author benben
 * @date 2022-05-31 15:57
 */
@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic topic1(){
        return new NewTopic("topic1", 3, (short)3);
    }

    @Bean
    public NewTopic topic2(){
        return new NewTopic("topic2", 3, (short)3);
    }
}
