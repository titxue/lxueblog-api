package com.titxu.blog.common.aop.LastLogin;

import java.lang.annotation.*;

/**
 * @author lxue
 * @date 2021/8/12
 * @apiNate
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LastLoginAnnotation {
    /**
     * 登录时间更新aop
     */
}
