package com.titxu.blog.vo;

import lombok.Data;

@Data
public class SysUserVo {
    // 不需要响应返回字段，注释即可
    private Long id;
    private String account;
    private Integer admin;
    private String avatar;
    private String createDate;
    private String lastLogin;
    private Integer deleted;
    private String email;
    private String mobilePhoneNumber;
    private String nickname;
    private String status;
    /**
     * 密码和盐值不需要出现在前端请求
     */
    //    private String password;
    //    private String salt;
}
