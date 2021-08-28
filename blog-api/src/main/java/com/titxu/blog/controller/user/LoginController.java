package com.titxu.blog.controller.user;


import com.titxu.blog.common.aop.LastLogin.LastLoginAnnotation;
import com.titxu.blog.common.aop.Log.LogAnnotation;
import com.titxu.blog.service.LoginService;
import com.titxu.blog.utils.result.Result;
import com.titxu.blog.vo.params.LoginParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    private LoginService loginService;



    @GetMapping("loginQr")
    public Result createCodeImg(){
        return loginService.loginQr();

    }


    /**
     * 密码登录
     * @param loginParams
     * @return
     */
    @PostMapping
    @LastLoginAnnotation
    @LogAnnotation(module="登录",operator="用户密码登录") //记录日志注解
    public Result login(@RequestBody LoginParams loginParams){
        return loginService.login(loginParams);

    }
}
