package com.example.base.aspect.newLog;

import java.lang.annotation.*;

/**
 * 接口日志注解（相当于一个方法配置多个@Transactional）
 * @author benben
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface InterfaceLog {

    /**
     * 模块
     */
    String module() default "";

    /**
     * 接口描述信息
     */
    String description() default "";

    /**
     * 是否记录接口入参
     */
    boolean recordInParam() default true;

    /**
     * 是否记录接口出参
     */
    boolean recordOutParam() default true;

    /**.
     * 接口类型
     */
    InterfaceType interfaceType() default InterfaceType.OTHER;

    enum InterfaceType{
        SELECT("查询"),
        INSERT("新增"),
        UPDATE("更新"),
        INSERT_OR_UPDATE("新增或更新"),
        DELETE("删除"),
        OTHER("其他"),
        UPLOAD("上传文件"),
        PREVIEW_OR_DOWNLOAD("预览或下载文件");

        private final String display;

        public String getDisplay() {
            return display;
        }

        InterfaceType(String display) {
            this.display = display;
        }
    }
}