package com.titxu.blog.utils.result;

public class CodeMsg {
    private int retCode;

    private String message;

    // 按照模块定义CodeMsg
    public static CodeMsg SUCCESS = new CodeMsg(200, "success");

    // 图片服务异常
    public static CodeMsg IMAGE_UPLOAD_ERROR = new CodeMsg(1001, "图片上传失败");

    // 通用异常


    public static CodeMsg SERVER_EXCEPTION = new CodeMsg(2000, "服务端异常");
    public static CodeMsg PARAMS_ERROR = new CodeMsg(2001, "参数有误。");
    public static CodeMsg REDIS_CACHE_SERVICE_EXCEPTION = new CodeMsg(2002, "Redis缓存服务异常");


    // 业务异常
    public static CodeMsg ACCOUNT_PASSWORD_NOT_EXIST = new CodeMsg(3000, "用户或密码不存在");
    public static CodeMsg TOKEN_NOT_EXIST = new CodeMsg(3001, "token不存在");
    public static CodeMsg TOKEN_CHECK_NOT = new CodeMsg(3002, "token校验失败");
    public static CodeMsg TOKEN_SESSION_TIME_OUT = new CodeMsg(3003, "会话超时");

    public static CodeMsg TOKEN_PERMISSION_NOT = new CodeMsg(3004, "无访问权限");
    public static CodeMsg ACCOUNT_EXIST = new CodeMsg(3005, "此用户存在");
    public static CodeMsg LOGIN_ERROR = new CodeMsg(3006, "登录失败");

    public static CodeMsg KKK_NOT_EXIST = new CodeMsg(4000, "？？？");
    public static CodeMsg ARTICLE_BODY_NOT_EXIST = new CodeMsg(4001, "文章为空");
    public static CodeMsg CATEGORY_NOT_EXIST = new CodeMsg(4002, "分类为空");
    public static CodeMsg LOGIN_NOT_CREATE_ARTICLE_ERROR = new CodeMsg(4003, "请登录后，重新创建文章");


    private CodeMsg(int retCode, String message) {
        this.retCode = retCode;

        this.message = message;

    }

    public int getRetCode() {
        return retCode;

    }

    public String getMessage() {
        return message;

    }

    public void setMessage(String message) {
        this.message = message;

    }

}