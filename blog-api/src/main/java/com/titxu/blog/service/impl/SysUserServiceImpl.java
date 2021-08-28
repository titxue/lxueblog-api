package com.titxu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.titxu.blog.dao.mapper.SysUserMapper;
import com.titxu.blog.dao.pojo.SysUser;
import com.titxu.blog.service.SysUserService;
import com.titxu.blog.service.TokenService;
import com.titxu.blog.vo.LoginUserVo;
import com.titxu.blog.vo.SysUserVo;
import com.titxu.blog.vo.UserVo;
import com.titxu.blog.utils.result.CodeMsg;
import com.titxu.blog.utils.result.Result;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private TokenService tokenService;

    @Override
    public SysUser findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser==null){
            sysUser = new SysUser();
            sysUser.setNickname("titxu");
        }
        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String password) {


        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.eq(SysUser::getPassword,password);
        queryWrapper.select(SysUser::getId,SysUser::getAccount,SysUser::getAvatar,SysUser::getNickname,SysUser::getLastLogin,SysUser::getCreateDate);
        queryWrapper.last("limit 1");


        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public Result findUserByToken(String token) {
        /**
         * 1.校验token合法性
         * 2.失败，返回错误
         * 3.成功返回对应的结果LoginUserVo
         */
        if (StringUtils.isBlank(token)){
            // 不存在校验
            log.info("用户token不存在");
            return Result.fail(CodeMsg.TOKEN_NOT_EXIST.getRetCode(), CodeMsg.TOKEN_NOT_EXIST.getMessage());
        }
        SysUserVo sysUserVo = tokenService.checkToken(token);
        if (sysUserVo==null){
            log.info("用户信息为空或者token校验失败");
            return Result.fail(CodeMsg.TOKEN_CHECK_NOT.getRetCode(),CodeMsg.TOKEN_CHECK_NOT.getMessage());
        }

        /**
         *     private Long id;
         *     private String account;
         *     private String nickname;
         *     private String avatar;
         */
        LoginUserVo loginUserVo = new LoginUserVo();
        BeanUtils.copyProperties(sysUserVo,loginUserVo);
//        loginUserVo.setId(sysUserVo.getId());
//        loginUserVo.setAccount(sysUserVo.getAccount());
//        loginUserVo.setNickname(sysUserVo.getNickname());
//        loginUserVo.setAvatar(sysUserVo.getAvatar());
//        loginUserVo.setLastlogin(sysUserVo.getLastLogin());
        log.info("用户信息获取成功");

        return Result.success(loginUserVo);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.last("limit 1");
        return this.sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public void save(SysUser sysUser) {
        this.sysUserMapper.insert(sysUser);

    }

    @Override
    public UserVo findUserVoById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser==null){
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("aaa/sss");
            sysUser.setNickname("titxu");
        }
        UserVo userVo =  new UserVo();
        BeanUtils.copyProperties(sysUser,userVo);
        return userVo;
    }
}
