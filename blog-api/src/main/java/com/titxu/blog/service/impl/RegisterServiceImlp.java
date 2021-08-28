package com.titxu.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.titxu.blog.dao.mapper.SysUserMapper;
import com.titxu.blog.dao.pojo.SysUser;
import com.titxu.blog.service.RegisterService;
import com.titxu.blog.service.SysUserService;
import com.titxu.blog.utils.JwtUtils;
import com.titxu.blog.utils.Tools;
import com.titxu.blog.vo.params.RegisterParams;
import com.titxu.blog.utils.result.CodeMsg;
import com.titxu.blog.utils.result.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class RegisterServiceImlp implements RegisterService {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    /**
     * 注册实现
     * 1.校验参数
     * 2.账号是否存在
     * 3.失败返回
     * 4.生成token
     * 5.存入redis
     * 6.事务回滚   插入数据需要保证失败回滚  （在 service 添加注解）
     * @param registerParams
     * @return
     */
    @Override
    public Result register(RegisterParams registerParams) {
        String account = registerParams.getAccount();
        String password = registerParams.getPassword();
        String nickname = registerParams.getNickname();
        if (StringUtils.isBlank(account) ||StringUtils.isBlank(password) || StringUtils.isBlank(nickname)){
            Result.fail(CodeMsg.PARAMS_ERROR.getRetCode(),CodeMsg.PARAMS_ERROR.getMessage());
        }
        SysUser sysUser = sysUserService.findUserByAccount(account);
        if (sysUser!=null){
            return Result.fail(CodeMsg.ACCOUNT_EXIST.getRetCode(), CodeMsg.ACCOUNT_EXIST.getMessage());
        }
        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setAdmin(1);
        sysUser.setAvatar("wwww");
        sysUser.setDeleted(0);
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setEmail("");
        sysUser.setMobilePhoneNumber("");
        sysUser.setPassword(Tools.getSlatPassword(password));
        sysUser.setStatus("");
        String token = JwtUtils.createToken(sysUser.getId());
        sysUserService.save(sysUser);
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);

        return Result.success(token);
    }
}
