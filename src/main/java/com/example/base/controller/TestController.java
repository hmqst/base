package com.example.base.controller;

import com.example.base.dto.CarDTO;
import com.example.base.enums.TypeEnum;
import com.example.base.mapper.CarMapper;
import com.example.base.po.CarPO;
import com.example.base.rabbitmq.RabbitFanoutConfig;
import com.example.base.rabbitmq.RabbitTopicConfig;
import com.example.base.utils.RedisUtil;
import com.example.base.utils.ResultBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author benben
 * @program base
 * @Description
 * @date 2021-03-22 15:50
 */
@RestController
@RequestMapping("test")
@Api(tags = "测试相关")
public class TestController {


    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private CarMapper carMapper;

    @Resource
    private ObjectMapper objectMapper;

    @ApiOperation("Jackson自定义全局序列化测试")
    @RequestMapping("jackson")
    public ResultBean jackson() {
        HashMap<Object, Object> map = new HashMap<>();
        HashMap nullMap = null;
        String nullStr = null;
        Object nullObject = null;
        ArrayList nullList = null;
        Boolean nullBoolean = null;
        Number nullnumber = null;
        map.put("key", nullStr);
        map.put("map", nullMap);
        map.put("object", nullObject);
        map.put("list", nullList);
        map.put("boolean", nullBoolean);
        map.put("number", nullnumber);
        map.put("objectMapper.getDateFormat()", objectMapper.getDateFormat().format(new Date()));
        return ResultBean.success(map);
    }

    @ApiOperation("MapStruct转换测试")
    @RequestMapping("mapStruct")
    public ResultBean mapStruct() {
        Map<String, Object> map = new HashMap<>();
        // PO转DTO
        CarPO car = new CarPO("make", 50, TypeEnum.FOUR, new Date());
        CarDTO carDto = carMapper.carToCarDto(car);
        map.put("PO转DTO", new HashMap<String, Object>() {{
            put("CarPO", car);
            put("CarDTO", carDto);
        }});
        // DTO转PO
        CarPO car1 = carMapper.carDtoToCar(carDto);
        map.put("DTO转PO", new HashMap<String, Object>() {{
            put("CarDTO", carDto);
            put("CarPO", car1);
        }});
        return ResultBean.success(map);
    }

    @ApiOperation("Redis存储对象测试")
    @RequestMapping("redis")
    public ResultBean redis() {
        CarPO car = new CarPO("make", 50, TypeEnum.FOUR, new Date());
        redisUtil.set("car", car);
        CarPO car1 = (CarPO) redisUtil.get("car");
        return ResultBean.success(car1);
    }

    @ApiOperation("RabbitMq（DirectExchange）")
    @RequestMapping("directExchange")
    public ResultBean directExchange() {
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("hello-direct", "hello-direct" + i);
            rabbitTemplate.convertAndSend("hello-direct1", "hello-direct1" + i);
            rabbitTemplate.convertAndSend("hello-direct2", "hello-direct2" + i);
        }
        return ResultBean.success();
    }

    @ApiOperation("RabbitMq（FanoutExchange）")
    @RequestMapping("fanoutExchange")
    public ResultBean fanoutExchange() {
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend(RabbitFanoutConfig.FANOUTNAME, null, "hello-fanout" + i);
        }
        return ResultBean.success();
    }

    @ApiOperation("RabbitMq（TopicExchange）")
    @RequestMapping("topicExchange")
    public ResultBean topicExchange() {
        rabbitTemplate.convertAndSend(RabbitTopicConfig.TOPICNAME, "xiaomi.news", "小米新闻");
        rabbitTemplate.convertAndSend(RabbitTopicConfig.TOPICNAME, "huawei.news", "华为新闻");
        rabbitTemplate.convertAndSend(RabbitTopicConfig.TOPICNAME, "phone.news", "手机新闻");
        rabbitTemplate.convertAndSend(RabbitTopicConfig.TOPICNAME, "huawei.phone", "华为手机");
        rabbitTemplate.convertAndSend(RabbitTopicConfig.TOPICNAME, "xiaomi.phone", "小米手机");
        return ResultBean.success();
    }

    @ApiOperation("直接输出文字测试")
    @RequestMapping("test")
    public String hello() {
        return "hello测试文字";
    }

}
