package com.example.base.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author benben
 * @program base
 * @Description Queue 通过routingkey 绑定到TopicExchange上，当消息到达TopicExchange后，TopicExchange根据消息的
 * routingkey 将消息路由到一个或者多个Queue上
 * @date 2021-04-01 8:58
 */
@Configuration
@Slf4j
public class RabbitTopicConfig {

    public static final String TOPICNAME = "topic";

    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(TOPICNAME, true, false);
    }

    @Bean
    Queue topicXiaomi() {
        return new Queue("hello-topic-xiaomi");
    }

    @Bean
    Queue topicHuaWei() {
        return new Queue("hello-topic-huawei");
    }

    @Bean
    Queue topicQueuePhone() {
        return new Queue("hello-topic-phone");
    }

    @Bean
    Binding topicBinding1() {
        // xiaomi开头的routingkey
        return BindingBuilder.bind(topicXiaomi()).to(topicExchange()).with("xiaomi.#");
    }

    @Bean
    Binding topicBinding2() {
        // huawei开头的routingkey
        return BindingBuilder.bind(topicHuaWei()).to(topicExchange()).with("huawei.#");
    }

    @Bean
    Binding topicBinding3() {
        // 包含phone的routingkey
        return BindingBuilder.bind(topicQueuePhone()).to(topicExchange()).with("#.phone.#");
    }

    @RabbitListener(queues = "hello-topic-xiaomi")
    public void fanoutHandle01(String msg) {
        log.info("XiaoMiTopicReceiver：" + msg);
    }

    @RabbitListener(queues = "hello-topic-huawei")
    public void fanoutHandle02(String msg) {
        log.info("HuaWeiTopicReceiver：" + msg);
    }

    @RabbitListener(queues = "hello-topic-phone")
    public void fanoutHandle11(String msg) {
        log.info("PhoneTopicReceiver：" + msg);
    }
}
