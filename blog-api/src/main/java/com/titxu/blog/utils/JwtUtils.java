package com.titxu.blog.utils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    private static final String jwtToken = "qweqwe124123412...,,,;;''";
    public static String createToken(Long userId){
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",userId);
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,jwtToken) // 加密算法 密钥jwtToken
                .setClaims(claims) // body数据 唯一 自行设置
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+24*60*60*60*1000));
        String token = jwtBuilder.compact();
        return token;
    }

    public static Map<String,Object> checkToken(String token){
        try {
            Jwt parse = Jwts.parser().setSigningKey(jwtToken).parse(token);
            return (Map<String, Object>) parse.getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // 测试
    public static void main(String[] args) {
        String token = JwtUtils.createToken(100L);
        System.out.println(token);
        Map<String, Object> checkToken = JwtUtils.checkToken(token);
        System.out.println(checkToken);
    }

}
