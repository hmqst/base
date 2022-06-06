package com.example.base.controller;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author benben
 * @date 2022-05-31 15:59
 */
@RestController
public class KafkaController {

    @Resource
    KafkaTemplate<String, String> kafkaTemplate;


    @GetMapping("/test/{msg}")
    public Object test(@PathVariable String msg){
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send("topic1", "key", msg);
        future.addCallback(result -> {
            System.out.println(msg + "--------------发送成功" + LocalDateTime.now().toString());
        }, ex -> {
            System.out.println(msg + "--------------发送失败" + LocalDateTime.now().toString());
        });
        return "00";
    }

    @KafkaListener(topics = "topic1",groupId = "MyGroup1")
    public void listenGroup(ConsumerRecord<String, String> record,
                            Acknowledgment ack) {
        String value = record.value();
        System.out.println(value);
        System.out.println(record);
        // ⼿动提交offset
        ack.acknowledge();
    }

    @KafkaListener(groupId = "testGroup", topicPartitions = { // 指定分区和主题
            @TopicPartition(topic = "topic1", partitions = {"0", "1"}),
            @TopicPartition(topic = "topic2", partitions = "0",
                    partitionOffsets = @PartitionOffset(partition = "1",
                            initialOffset = "100")) // 自定偏移量读取
    },concurrency = "3")//concurrency就是同组下的消费者个数，就是并发消费数（自动创建三个相同的方法），建议⼩于等于分区总数
    public void listenGroupTwo(ConsumerRecord<String, String> record,
                            Acknowledgment ack) {
        String value = record.value();
        System.out.println(value);
        System.out.println(record);
        // ⼿动提交offset
        ack.acknowledge();
    }
}
