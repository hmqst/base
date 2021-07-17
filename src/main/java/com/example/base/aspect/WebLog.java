package com.example.base.aspect;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * @author benben
 * @program base
 * @Description Controller日志记录
 * @date 2021-03-22 15:50
 */
@Data
@ToString
public class WebLog {

    /**
     * URI
     */
    private String uri;

    /**
     * URL
     */
    private String url;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 操作描述
     */
    private String description;

    /**
     * 操作时间
     */
    private Date startTime;

    /**
     * 消耗时间
     */
    private String spendTime;

    /**
     * 请求类型
     */
    private String method;


    /**
     * 请求参数
     */
    private Object parameter;

    /**
     * 返回结果
     */
    private Object result;

}
