package com.titxu.blog.common.aop.Log;

import com.alibaba.fastjson.JSON;
import com.titxu.blog.utils.HttpContextUtils;
import com.titxu.blog.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author lxue
 * @date 2021/8/10
 * @apiNate
 */
@Component
@Aspect
@Slf4j
public class LogAspect {
    @Pointcut("@annotation(com.titxu.blog.common.aop.Log.LogAnnotation)")
    public void pt(){}


    @Around("pt()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable{

        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = joinPoint.proceed();
        //执行时间 毫秒
        long time = System.currentTimeMillis()-beginTime;
        //保存日志
        recordlog(joinPoint,time);
        return result;
    }


    /**
     * aop记录日志
     * @param joinPoint
     * @param time
     */
    private void recordlog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogAnnotation annotation = method.getAnnotation(LogAnnotation.class);
        log.info("--------------开始记录日志--------------");
        log.info("模块：{}",annotation.module());
        log.info("执行操作：{}",annotation.operator());

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.info("请求方法：{}.{}",className,methodName);

        //请求参数
        Object[] args = joinPoint.getArgs();
        StringBuilder params = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null) {
                params.append(JSON.toJSONString(args[i]));
            }
        }
        if (StringUtils.isBlank(params)){
            params.append("这个接口没有参数");
        }
        log.info("参数：{}",params);
        //获取request 设置ip地址
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        log.info("客户端请求IP:{}", IpUtils.getIpAddr(request));
        log.info("客户端请求端口：{}",IpUtils.getPort(request));
        log.info("客户端请求地址：{}:{}",IpUtils.getIpAddr(request),IpUtils.getPort(request));

        log.info("执行时间：{} 毫秒",time);
        log.info("--------------结束记录日志--------------");
    }
}
