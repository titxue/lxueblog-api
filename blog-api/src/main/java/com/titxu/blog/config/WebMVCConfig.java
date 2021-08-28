package com.titxu.blog.config;

import com.titxu.blog.hander.LoginInterceptorHander;
import com.titxu.blog.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;


@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptorHander loginInterceptorHander;

    @Autowired
    private HttpMessageConverters httpMessageConverters;

    /**
     *  MappingJackson2HttpMessageConverter 实现了HttpMessageConverter 接口，
     *  httpMessageConverters.getConverters() 返回的对象里包含了MappingJackson2HttpMessageConverter
     * @return
     */
    @Bean
    public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter(new Tools.JacksonMapper());
    }


    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.addAll(httpMessageConverters.getConverters());
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        WebMvcConfigurer.super.addCorsMappings(registry);
        registry.addMapping("/**").allowedOrigins("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /**
         * 拦截test接口
         */
        registry.addInterceptor(loginInterceptorHander)
                .addPathPatterns("/test")
                .addPathPatterns("/comments/create/change")
                .addPathPatterns("/articles/create")
                .addPathPatterns("/upload/*")
        ;
    }
}
