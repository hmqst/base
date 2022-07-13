package com.example.base.aspect.newLog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 * 平台内部接口操作日志
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogPo implements Serializable {
    private Long logId;

    /**
     * 访问 IP
     */
    private String fromIp;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 用时（毫秒）
     */
    private Long useTime;

    /**
     * 请求类型
     */
    private String requestType;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 返回数据
     */
    private String resultData;

    /**
     * 接口类型
     */
    private InterfaceLog.InterfaceType interfaceType;

    /**
     * 接口所属模块
     */
    private String interfaceModule;

    /**
     * 接口描述
     */
    private String interfaceDescription;

    /**
     * 接口请求路径
     */
    private String interfaceUri;

    /**
     * 接口请求完整地址
     */
    private String interfaceUrl;
}