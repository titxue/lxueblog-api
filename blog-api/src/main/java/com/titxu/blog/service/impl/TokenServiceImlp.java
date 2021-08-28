package com.titxu.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.titxu.blog.dao.pojo.SysUser;
import com.titxu.blog.service.TokenService;
import com.titxu.blog.utils.JwtUtils;
import com.titxu.blog.vo.SysUserVo;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TokenServiceImlp implements TokenService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    /**
     * 根据token返回SysUserVo对象
     * @param token
     * @return
     */
    @Override
    public SysUserVo checkToken(String token) {
        Map<String, Object> stringObjectMap = JwtUtils.checkToken(token);
        if (stringObjectMap==null){
            return null;
        }
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isBlank(userJson)){
            // redis是否存在
            return null;
        }
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        SysUserVo sysUserVo = copy(sysUser);

        return sysUserVo;
    }

    /**
     * 根据token返回SysUser
     * @param token
     * @return
     */
    @Override
    public SysUser findUserByToken(String token) {
        Map<String, Object> map = JwtUtils.checkToken(token);
        if (map==null){
            return null;
        }
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isBlank(userJson)){
            // redis是否存在
            return null;
        }
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);

        return sysUser;
    }

    private SysUserVo copy(SysUser sysUser){
        SysUserVo sysUserVo = new SysUserVo();
        BeanUtils.copyProperties(sysUser,sysUserVo);


        sysUserVo.setCreateDate(new DateTime(sysUser.getCreateDate()).toString("yyyy-MM-dd HH:mm:ss"));
        sysUserVo.setLastLogin(new DateTime(sysUser.getLastLogin()).toString("yyyy-MM-dd HH:mm:ss"));
//        System.out.println(sysUser.getCreateDate());


        return sysUserVo;
    }
}
