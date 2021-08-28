package com.titxu.blog.utils;

import com.titxu.blog.dao.pojo.SysUser;
import com.titxu.blog.vo.SysUserVo;

public class UserThreadLocal {
    private UserThreadLocal() {
    }
    private static final ThreadLocal<SysUserVo>  LOCAL = new ThreadLocal<>();

    public static void put(SysUserVo sysUserVo){
        LOCAL.set(sysUserVo);

    }
    public static SysUserVo get(){
        return LOCAL.get();
    }
    public static void remove(){
        LOCAL.remove();
    }
}
