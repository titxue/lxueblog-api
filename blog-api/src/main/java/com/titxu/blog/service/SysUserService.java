package com.titxu.blog.service;

import com.titxu.blog.dao.pojo.SysUser;
import com.titxu.blog.vo.UserVo;
import com.titxu.blog.utils.result.Result;

public interface SysUserService {
    SysUser findUserById(Long id);

    SysUser findUser(String account, String password);

    /**
     * 根据token查询用户信息
     * @param token
     * @return
     */
    Result findUserByToken(String token);


    SysUser findUserByAccount(String account);

    void save(SysUser sysUser);

    /**
     * 根据作者id查询user信息
     * @param id
     */
    UserVo findUserVoById(Long id);
}
