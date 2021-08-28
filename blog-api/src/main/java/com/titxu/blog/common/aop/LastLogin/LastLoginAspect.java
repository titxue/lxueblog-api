package com.titxu.blog.common.aop.LastLogin;

import com.alibaba.fastjson.JSON;
import com.titxu.blog.dao.mapper.SysUserMapper;
import com.titxu.blog.dao.pojo.SysUser;
import com.titxu.blog.service.ThreadService;
import com.titxu.blog.service.TokenService;
import com.titxu.blog.vo.params.LoginParams;
import com.titxu.blog.utils.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lxue
 * @date 2021/8/12
 * @apiNate
 */
@Component
@Aspect
@Slf4j
public class LastLoginAspect {

    @Autowired
    private ThreadService threadService;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private TokenService tokenService;

    private LoginParams login;

    @Pointcut("@annotation(com.titxu.blog.common.aop.LastLogin.LastLoginAnnotation)")
    public void pt(){}

    /**
     * 1.获取登录方法前传入的参数
     * 2.返回login对象
     * （非必要方法，废弃方法）
     * （已经根据token获取user对象）
     * @param joinPoint
     */
    @Before("pt()")
    public void before(JoinPoint joinPoint) {
        //请求参数
        Object[] args = joinPoint.getArgs();
        String params = JSON.toJSONString(args[0]);
        LoginParams loginParams = new LoginParams();
        login = JSON.parseObject(params, loginParams.getClass());
        log.info("参数：{}",login);
    }

    /**
     * 环绕aop
     * 1.登录后获取登录返回token
     * 2.根据token确认登录用户
     * 3.更新登录用户的上次登录时间为最新登录时间
     * 4.日志输出 上次登录时间
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("pt()")
    public Object lastLoginUpdate(ProceedingJoinPoint point)throws Throwable {
        Result proceed = (Result) point.proceed();

        String token = proceed.getData().toString();
        SysUser sysUser = tokenService.findUserByToken(token);
        threadService.updateLastLoginDate(sysUserMapper,sysUser);
        // new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm")
        log.info("上次登录时间：{}",new DateTime(sysUser.getLastLogin()).toString("yyyy-MM-dd HH:mm:ss"));

        return proceed;


    }
}
