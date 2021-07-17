package com.example.base.utils;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultBean implements Serializable {

    public static final int SUCCESS = 0;

    public static final int FAIL = -1;

    public static final String SUCCESS_MESSAGE = "成功";

    public static final String FAIL_MESSAGE = "失败";

    /**
     * 返回码，全局默认成功码为0，失败为-1
     */
    private int code;

    /**
     * 返回错误信息
     */
    private String message;

    /**
     * 返回对象，可以是简单对象，封装处理后的对象，或者MAP
     */
    private Object object;

    public static ResultBean success() {
        return new ResultBean(SUCCESS, SUCCESS_MESSAGE, null);
    }

    public static ResultBean success(Object object) {
        return new ResultBean(SUCCESS, SUCCESS_MESSAGE, object);
    }

    public static ResultBean success(String message) {
        return new ResultBean(SUCCESS, message, null);
    }

    public static ResultBean success(String message, Object object) {
        return new ResultBean(SUCCESS, message, object);
    }

    public static ResultBean fail() {
        return new ResultBean(FAIL, FAIL_MESSAGE, null);
    }

    public static ResultBean fail(String message) {
        return new ResultBean(FAIL, message, null);
    }

    public static ResultBean fail(Integer code, String message) {
        return new ResultBean(code, message, null);
    }

}
