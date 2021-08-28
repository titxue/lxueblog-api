package com.titxu.blog.service;

import com.titxu.blog.vo.params.RegisterParams;
import com.titxu.blog.utils.result.Result;
import org.springframework.transaction.annotation.Transactional;


@Transactional //事务回滚
public interface RegisterService {


    /**
     * 注册用户
     * @param registerParams
     * @return
     */
    Result register(RegisterParams registerParams);
}
