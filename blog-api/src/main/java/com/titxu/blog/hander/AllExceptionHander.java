package com.titxu.blog.hander;


import com.titxu.blog.utils.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


// 对加了controller注解的方法进行拦截处理
// AOP实现
@ControllerAdvice
public class AllExceptionHander {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result doException(Exception e){
        e.printStackTrace();

        return Result.fail(0,"系统异常");
    }

}
