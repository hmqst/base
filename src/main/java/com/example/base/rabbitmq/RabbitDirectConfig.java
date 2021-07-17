package com.example.base.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author benben
 * @program base
 * @Description DirectExchange 的路由策略是将消息队列绑定到一个DirectExchange 上，当一条消息到达
 * DirectExchange 时会被转发到与该条消息routing key相同的Queue 上，例如消息队列名为
 * hello-queue”，则routing key为“hello-queue”的消息会被该消息队列接收。
 * <p>
 * 使用此模式DirectExchange和Binding无效  默认是负载均衡的
 * @date 2021-03-30 15:06
 */
@Configuration
@Slf4j
public class RabbitDirectConfig {

    public final static String DIRECTNAME = "direct";

    @Bean
    Queue directQueue() {
        // 队列
        return new Queue("hello-direct");
    }

    @Bean
    Queue directQueue1() {
        // 队列
        return new Queue("hello-direct1");
    }

    @Bean
    Queue directQueue2() {
        // 队列
        return new Queue("hello-direct2");
    }

    //@Bean
    DirectExchange directExchange() {
        // 路由策略 重启后是否依然有效  长期未用时是否删除
        return new DirectExchange(DIRECTNAME, true, false);
    }

    //@Bean
    Binding directBinding() {
        return BindingBuilder.bind(directQueue1()).to(directExchange()).with("routingKey-direct");
    }

    @RabbitListener(queues = "hello-direct")
    public void directHandle01(String msg) {
        log.info("零一DirectReceiver：" + msg);
    }

    @RabbitListener(queues = "hello-direct")
    public void directHandle02(String msg) {
        log.info("零二DirectReceiver：" + msg);
    }

    @RabbitListener(queues = "hello-direct")
    public void directHandle03(String msg) {
        log.info("零三DirectReceiver：" + msg);
    }

    @RabbitListener(queues = "hello-direct1")
    public void directHandle11(String msg) {
        log.info("一一DirectReceiver：" + msg);
    }

    @RabbitListener(queues = "hello-direct1")
    public void directHandle12(String msg) {
        log.info("一二DirectReceiver：" + msg);
    }

    @RabbitListener(queues = "hello-direct2")
    public void directHandle21(String msg) {
        log.info("二一DirectReceiver：" + msg);
    }

    @RabbitListener(queues = "hello-direct2")
    public void directHandle22(String msg) {
        log.info("二二DirectReceiver：" + msg);
    }
}
