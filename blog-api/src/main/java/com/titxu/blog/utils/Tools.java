package com.titxu.blog.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.apache.commons.codec.digest.DigestUtils;

public class Tools {
    // 密码 盐值
    private static final String slat = "zxcvbnm,./";


    public static String getSlatPassword(String password){
        return DigestUtils.md5Hex(password+slat);
    }


    /**
     * 返回Long转换为String
     * @author Jingly
     */
    public static class JacksonMapper extends ObjectMapper {

        public JacksonMapper() {
            super();
            this.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
            this.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
            this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            this.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
            simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
            simpleModule.addSerializer(long.class, ToStringSerializer.instance);
            registerModule(simpleModule);

        }
    }
}
