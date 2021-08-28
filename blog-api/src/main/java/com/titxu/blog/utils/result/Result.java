package com.titxu.blog.utils.result;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {
    private boolean success;
    private int code;
    private String msg;
    private Object data;


    public static Result success(Object data){
        return new Result(true, CodeMsg.SUCCESS.getRetCode(),CodeMsg.SUCCESS.getMessage(),data);
    }

    public static Result fail(int code,String msg){
        return new Result(false,code,msg,null);
    }
}
