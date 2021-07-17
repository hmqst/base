package com.example.base.config.mybatis.transaction;

import java.lang.annotation.*;

/**
 * 多数据源事务注解（相当于一个方法配置多个@Transactional）
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface MultiTransactional {

    String[] value() default {};
}