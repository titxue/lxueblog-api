package com.titxu.blog.hander;


import com.alibaba.fastjson.JSON;
import com.titxu.blog.service.TokenService;
import com.titxu.blog.utils.UserThreadLocal;
import com.titxu.blog.vo.SysUserVo;
import com.titxu.blog.utils.result.CodeMsg;
import com.titxu.blog.utils.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoginInterceptorHander implements HandlerInterceptor {
    @Autowired
    private TokenService tokenService;
    /**
     * 处理请求之前执行
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /**
         * 拦截器
         * 登录验证
         *
         */
        String token = request.getHeader("Authorization");
        log.info("-----------Request Start----------");
//        String requestURI = request.getRequestURI();
        StringBuffer requestURL = request.getRequestURL();
        log.info("Request Url: {}",requestURL);
        String method = request.getMethod();
        log.info("Request Method:{}",method);
        log.info("Token: {}",token);
        log.info("-----------Request End----------");


        if (!(handler instanceof HandlerMethod)){
            // 不拦截资源请求
            return true;
        }

        if (StringUtils.isBlank(token)){
            Result result = Result.fail(CodeMsg.TOKEN_NOT_EXIST.getRetCode(), CodeMsg.TOKEN_NOT_EXIST.getMessage());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false; // 空值 拦截
        }
        SysUserVo sysUserVo = tokenService.checkToken(token);

        if (sysUserVo==null){
            Result result = Result.fail(CodeMsg.TOKEN_CHECK_NOT.getRetCode(), CodeMsg.TOKEN_CHECK_NOT.getMessage());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false; // token 验证失败 拦截
        }
        UserThreadLocal.put(sysUserVo);


        return true; // 放行
    }

    /**
     * 处理请求之后执行
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 相应后执行
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.remove();
    }
}
