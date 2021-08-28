package com.titxu.blog.common.aop.Cache;

import java.lang.annotation.*;

/**
 * @author lxue
 * @date 2021/8/15
 * @apiNate
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheAnnotation {
    // 唯一 key name
    String name() default "";
    // 过期时间
    long expires() default 1 * 60 * 1000;

}
