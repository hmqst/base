package com.example.base.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult<T> implements Serializable {

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
    private T object;

    public static <T> CommonResult<T> success() {
        return new CommonResult<T>(SUCCESS, SUCCESS_MESSAGE, null);
    }

    public static <T> CommonResult<T> success(T object) {
        if (object instanceof String) {
            return new CommonResult<T>(SUCCESS, (String) object, null);
        }
        return new CommonResult<T>(SUCCESS, SUCCESS_MESSAGE, object);
    }

    public static <T> CommonResult<T> success(String message) {
        return new CommonResult<T>(SUCCESS, message, null);
    }

    public static <T> CommonResult<T> success(String message, T object) {
        return new CommonResult<T>(SUCCESS, message, object);
    }

    public static <T> CommonResult<T> fail() {
        return new CommonResult<T>(FAIL, FAIL_MESSAGE, null);
    }

    public static <T> CommonResult<T> fail(String message) {
        return new CommonResult<T>(FAIL, message, null);
    }

    public static <T> CommonResult<T> fail(Integer code, String message) {
        return new CommonResult<T>(code, message, null);
    }

}
