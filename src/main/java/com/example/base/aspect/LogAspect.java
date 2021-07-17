package com.example.base.aspect;

import com.example.base.utils.RequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.*;

@Component
@Aspect
@Slf4j
public class LogAspect {

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 定义切入点
     */
    @Pointcut("execution(* com.example.base.controller.*.*(..))")
    public void log() {
    }

    /**
     * 前置通知
     */
    @Before(value = "log()")
    public void before(JoinPoint jp) {
        // 获取拦截到的方法名
        String name = jp.getSignature().getName();
        log.info(name + " 方法开始执行");
    }

    /**
     * 最终通知
     */
    @After(value = "log()")
    public void after(JoinPoint jp) {
        // 获取拦截到的方法名
        String name = jp.getSignature().getName();
        log.info(name + " 方法结束执行");
    }

    /**
     * 后置通知
     */
    @AfterReturning(value = "log()", returning = "result")
    public void afterReturning(JoinPoint jp, Object result) throws Exception {
        // 获取拦截到的方法名
        String name = jp.getSignature().getName();
        log.info(name + " 方法返回值为：" + objectMapper.writeValueAsString(result));
    }

    /**
     * 异常通知
     */
    @AfterThrowing(value = "log()", throwing = "e")
    public void afterThrowing(JoinPoint jp, Exception e) {
        // 获取拦截到的方法名
        String name = jp.getSignature().getName();
        log.info(name + " 方法异常，异常是：" + e.getMessage());
    }

    @Around("log()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录请求信息(通过Logstash传入Elasticsearch)
        WebLog webLog = new WebLog();
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        // Swagger描述信息
        if (method.isAnnotationPresent(ApiOperation.class)) {
            ApiOperation log = method.getAnnotation(ApiOperation.class);
            webLog.setDescription(log.value());
        }
        // IP地址
        webLog.setIp(RequestUtil.getIp(request));
        // 请求类型
        webLog.setMethod(request.getMethod());
        // 请求参数
        webLog.setParameter(RequestUtil.getParameter(method, joinPoint.getArgs()));
        // 返回结果
        webLog.setResult(result);
        // 方法消耗时间
        long speedTime = endTime - startTime;
        webLog.setSpendTime(speedTime / 1000 + "." + speedTime % 1000 + "秒");
        // 方法开始时间
        webLog.setStartTime(new Date(startTime));
        // uri（不带服务器地址）
        webLog.setUri(request.getRequestURI());
        // url（带服务器地址）
        webLog.setUrl(request.getRequestURL().toString());
        log.info(objectMapper.writeValueAsString(webLog));
        return result;
    }
}
