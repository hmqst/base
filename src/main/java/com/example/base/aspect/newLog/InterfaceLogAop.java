package com.example.base.aspect.newLog;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.*;
import org.apache.ibatis.javassist.bytecode.CodeAttribute;
import org.apache.ibatis.javassist.bytecode.LocalVariableAttribute;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置切面
 * 扫描 @MInterfaceLog 注解的方法执行日志记录
 *
 * @author benben
 */
@Aspect
@Component
@Slf4j
public class InterfaceLogAop {

    @Pointcut("@annotation(com.example.base.aspect.newLog.InterfaceLog)")
    public void pointCut() {
    }

    @Around("pointCut() && @annotation(interfaceLog)")
    public Object recordInterfaceLog(ProceedingJoinPoint joinPoint, InterfaceLog interfaceLog) throws Throwable {
        // 获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        boolean recordInParam = interfaceLog.recordInParam();
        String requestParams;
        if (recordInParam){
            requestParams = JSON.toJSONString(getParameters(joinPoint));
        }else {
            requestParams = "{}";
        }
        Instant startTime = Instant.now();
        Object result = joinPoint.proceed();
        Instant endTime = Instant.now();

        LogPo.LogPoBuilder builder = LogPo.builder();
        Duration duration = Duration.between(startTime, endTime);
        // ip
        builder.fromIp(getIpAddr(request))
                // 方法开始时间
                .startTime(Date.from(startTime))
                // 方法结束时间
                .endTime(Date.from(endTime))
                // 方法耗时（毫秒）
                .useTime(duration.toMillis())
                // 请求类型
                .requestType(request.getMethod())
                // 请求参数
                .requestParam(requestParams)
                // 返回结果
                .resultData(interfaceLog.recordOutParam() ? String.valueOf(result) : "{}")
                // 接口类型
                .interfaceType(interfaceLog.interfaceType())
                // 接口模块
                .interfaceModule(interfaceLog.module())
                // 接口描述
                .interfaceDescription(interfaceLog.description())
                // uri（不带服务器地址）
                .interfaceUri(request.getRequestURI())
                // url（带服务器地址）
                .interfaceUrl(request.getRequestURL().toString());
        LogPo logPo = builder.build();
        log.info("【平台内部接口日志】" + JSON.toJSONString(logPo));
        return result;
    }


    /**
     * 获取请求参数
     */
    private Map<String, Object> getParameters(JoinPoint joinPoint) throws NotFoundException {
        String className = joinPoint.getTarget().getClass().getName();
        Object[] args = joinPoint.getArgs();

        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        String methodName = method.getName();

        Map<String, Object> map = new HashMap<>();
        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(this.getClass());
        pool.insertClassPath(classPath);

        CtClass cc = pool.get(className);
        CtMethod cm = cc.getDeclaredMethod(methodName);
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            // exception
            return map;
        }
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < cm.getParameterTypes().length; i++) {
            if (args[i] instanceof ServletRequest || args[i] instanceof ServletResponse || args[i] instanceof MultipartFile) {
                // ServletRequest不能序列化，从入参里排除，否则报异常：java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
                // ServletResponse不能序列化 从入参里排除，否则报异常：java.lang.IllegalStateException: getOutputStream() has already been called for this response
                continue;
            }
            // paramNames即参数名
            map.put(attr.variableName(i + pos), args[i]);
        }
        return map;
    }

    public static String getIpAddr(HttpServletRequest request)
    {
        if (request == null)
        {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }
}
