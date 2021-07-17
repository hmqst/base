package com.example.base.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author benben
 * @program base
 * @Description FanoutExchange 的数据交换策略是把所有到达FanoutExchange 的消息转发给所有与它绑定的
 * Queue ，在这种策略中，routingkey 将不起任何作用，
 * @date 2021-03-30 16:33
 */
@Configuration
@Slf4j
public class RabbitFanoutConfig {

    public static final String FANOUTNAME = "fanout";

    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUTNAME, true, false);
    }

    @Bean
    Queue fanoutQueue1() {
        return new Queue("hello-queue1");
    }

    @Bean
    Queue fanoutQueue2() {
        return new Queue("hello-queue2");
    }

    @Bean
    Queue fanoutQueue() {
        return new Queue("hello-queue");
    }

    @Bean
    Binding fanoutBinding1() {
        return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange());
    }

    @Bean
    Binding fanoutBinding2() {
        return BindingBuilder.bind(fanoutQueue2()).to(fanoutExchange());
    }

    @Bean
    Binding fanoutBinding3() {
        return BindingBuilder.bind(fanoutQueue()).to(fanoutExchange());
    }

    @RabbitListener(queues = "hello-queue")
    public void fanoutHandle01(String msg) {
        log.info("零一QueueReceiver：" + msg);
    }

    @RabbitListener(queues = "hello-queue")
    public void fanoutHandle02(String msg) {
        log.info("零二QueueReceiver：" + msg);
    }

    @RabbitListener(queues = "hello-queue1")
    public void fanoutHandle11(String msg) {
        log.info("一一QueueReceiver：" + msg);
    }

    @RabbitListener(queues = "hello-queue1")
    public void fanoutHandle12(String msg) {
        log.info("一二QueueReceiver：" + msg);
    }

    @RabbitListener(queues = "hello-queue2")
    public void fanoutHandle21(String msg) {
        log.info("二一QueueReceiver：" + msg);
    }

    @RabbitListener(queues = "hello-queue2")
    public void fanoutHandle22(String msg) {
        log.info("二二QueueReceiver：" + msg);
    }
}
