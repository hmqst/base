package com.example.base.config;

import com.example.base.utils.ResultBean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常捕获处理（Exception）
 * @author benben
 * @date 2021-03-22 15:50
 */
@ControllerAdvice
public class MyGlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultBean customException(Exception e){
        e.printStackTrace();
        return ResultBean.fail(500, "服务器异常：" + e.getMessage());
    }
}
