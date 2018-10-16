//package com.lanpang.apigateway;
//
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
//import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
//import org.springframework.stereotype.Component;
//
///**
// * @program: api-gateway
// * @description: 代码中动态更新配置文件
// * @author: yanghao
// * @create: 2018-09-28 13:03
// **/
//@Component
//public class ZuulConfig {
//
//    @ConfigurationProperties("zuul")
//    @RefreshScope
//    public ZuulProperties zuulProperties() {
//        return new ZuulProperties();
//    }
//}
