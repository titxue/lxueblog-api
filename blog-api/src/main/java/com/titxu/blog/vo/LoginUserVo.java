package com.titxu.blog.vo;


import lombok.Data;

@Data
public class LoginUserVo {
    private Long id;
    private String account;
    private String nickname;
    private String avatar;
    private String lastLogin;
    private String createDate;
//    private String email;


}
