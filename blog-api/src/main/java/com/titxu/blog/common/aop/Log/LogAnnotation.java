package com.titxu.blog.common.aop.Log;

import java.lang.annotation.*;

/**
 * @author lxue
 * @date 2021/8/10
 * @apiNate
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnotation {
    String module() default "";
    String operator() default "";
}
