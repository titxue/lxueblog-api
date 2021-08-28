package com.titxu.blog.controller.user;

import com.titxu.blog.service.RegisterService;
import com.titxu.blog.vo.params.RegisterParams;
import com.titxu.blog.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    /**
     * 注册
     * @param registerParams
     * @return
     */
    @PostMapping
    public Result register(@RequestBody RegisterParams registerParams){
        return registerService.register(registerParams);

    }
}
