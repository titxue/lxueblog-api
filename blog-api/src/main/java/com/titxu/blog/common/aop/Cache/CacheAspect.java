package com.titxu.blog.common.aop.Cache;

import com.alibaba.fastjson.JSON;
import com.titxu.blog.utils.result.CodeMsg;
import com.titxu.blog.utils.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * @author lxue
 * @date 2021/8/15
 * @apiNate
 */
@Slf4j
@Component
@Aspect
public class CacheAspect {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Pointcut("@annotation(com.titxu.blog.common.aop.Cache.CacheAnnotation)")
    public void pt() {
    }

    @Around("pt()")
    public Object around(ProceedingJoinPoint joinPoint)  {
        try {
            Signature signature = joinPoint.getSignature();
            // 类名
            String className = joinPoint.getTarget().getClass().getSimpleName();
            // 调用的方法名
            String methodName = signature.getName();

            Class[] parameterTypes = new Class[joinPoint.getArgs().length];
            Object[] args = joinPoint.getArgs();
            // 参数
            StringBuilder params = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                if (args[i] != null) {
                    params.append(JSON.toJSONString(args[i]));
                    parameterTypes[i] = args[i].getClass();
                } else {
                    parameterTypes[i] = null;
                }
            }
            if (StringUtils.isNotEmpty(params.toString())) {
                // 加密防止字符太长或者转义获取不到
                params = new StringBuilder(DigestUtils.md5Hex(params.toString()));
            }
            Method method = joinPoint.getSignature().getDeclaringType().getMethod(methodName, parameterTypes);
            // 获取cache注解
            CacheAnnotation annotation = method.getAnnotation(CacheAnnotation.class);
            // 缓存过期时间
            long expires = annotation.expires();
            //缓存名称
            String name = annotation.name();
            // 优先redis获取
            String redisKey = className + ":" + methodName + ":" + params;
            //System.out.println(redisKey);
            // 905968d8717edb0576035af136244f34
            // 905968d8717edb0576035af136244f34
            String redisValue = redisTemplate.opsForValue().get(redisKey);
            if (StringUtils.isNotEmpty(redisValue)){
                log.info("获取redis cache --- {}",className+"."+methodName);
                return JSON.parseObject(redisValue, Result.class);
            }
            Object proceed = joinPoint.proceed();
            // Duration.ofMillis(expires) 缓存持续时间  格式化
            redisTemplate.opsForValue().set(redisKey,JSON.toJSONString(proceed), Duration.ofMillis(expires));
            log.info("存入redis cache --- {}",className+"."+methodName);
            return proceed;
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
        return Result.fail(
                CodeMsg.REDIS_CACHE_SERVICE_EXCEPTION.getRetCode(),
                CodeMsg.REDIS_CACHE_SERVICE_EXCEPTION.getMessage()
        );




    }


}
