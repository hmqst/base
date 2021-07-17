package com.example.base.config;

import com.example.base.utils.ResultBean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author benben
 * @program base
 * @Description 全局异常捕获处理（Exception）
 * @date 2021-03-22 15:50
 */
@ControllerAdvice
public class MyGlobalExceptionHandler {

    /**
     * 返回页面
     */
    /*public ModelAndView customException(SysException e){
        ModelAndView mv = new ModelAndView();
        mv.addObject("message",e.getMessage());
        mv.addObject("code",e.getCode());
        mv.setViewName("myerror");
        return mv;
    }*/

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultBean customException(Exception e){
        e.printStackTrace();
        return ResultBean.fail(500, "服务器异常：" + e.getMessage());
    }
}