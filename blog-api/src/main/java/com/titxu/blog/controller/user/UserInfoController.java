package com.titxu.blog.controller.user;


import com.titxu.blog.service.SysUserService;
import com.titxu.blog.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserInfoController {

    @Autowired
    private SysUserService sysUserService;


    /**
     * 用户信息
     * @param token
     * @return
     */
    @PostMapping("userinfo")
    public Result userInfo(@RequestHeader("Authorization") String token){
        return sysUserService.findUserByToken(token);
    }

}
