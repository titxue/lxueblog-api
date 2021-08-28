package com.titxu.blog.service;

import com.titxu.blog.dao.pojo.SysUser;
import com.titxu.blog.vo.SysUserVo;

public interface TokenService {
    /**
     * 校验token
     * @param token
     * @return
     */
    SysUserVo checkToken(String token);
    SysUser findUserByToken(String token);
}
