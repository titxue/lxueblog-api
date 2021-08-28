package com.titxu.blog.service;

import com.titxu.blog.utils.result.Result;
import com.titxu.blog.vo.params.LoginParams;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface LoginService {

    /**
     * 登录
     *
     * @param loginParams
     * @return
     */
    Result login(LoginParams loginParams);

    /**
     * 退出登录
     *
     * @param token
     * @return
     */
    Result logout(String token);

    Result loginQr();
}
