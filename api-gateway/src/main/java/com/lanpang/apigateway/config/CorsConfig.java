package com.lanpang.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * @program: api-gateway
 * @description: 跨域配置 使用Filter 处理跨域请求，即CORS（跨来源资源共享）。
 * @author: yanghao
 * @create: 2018-10-09 16:38
 **/
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();

        config.setAllowedMethods(Arrays.asList("*"));//真实请求允许的方法
        config.setAllowedMethods(Arrays.asList("*"));//真实请求允许的方法
        config.setAllowCredentials(true);//是否允许用户发送、处理 cookie
        config.setAllowedHeaders(Arrays.asList("*"));//: 服务器允许使用的字段 请求头
        config.setAllowedOrigins(Arrays.asList("*"));// 允许跨域访问的域名
        config.setMaxAge(3600L);//预检请求的有效期，单位为秒。有效期内，不会重复发送预检请求

        //配置对那些域名
        source.registerCorsConfiguration("/**",config);
        return new CorsFilter(source);
    }
}
