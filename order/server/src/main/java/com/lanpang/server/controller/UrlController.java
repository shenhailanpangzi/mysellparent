package com.lanpang.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: order
 * @description: 获取全部的url
 * @author: yanghao
 * @create: 2018-10-10 14:41
 **/
@RestController
public class UrlController {

    @Autowired
    WebApplicationContext applicationContext;

    @GetMapping("/getAllUrl")
    @ResponseBody
    public List<String> getAllUrl(){
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        //获取url与类和方法的对应信息
        Map<RequestMappingInfo,HandlerMethod> map = mapping.getHandlerMethods();
        List<String> urlList = new ArrayList<String>();
        for (RequestMappingInfo info : map.keySet()){
            //获取url的Set集合，一个方法可能对应多个url
            Set<String> patterns = info.getPatternsCondition().getPatterns();
            for (String url : patterns){
                urlList.add(url);
            }
        }
        return urlList;
    }

    @GetMapping("/hello")
    public String hello(){
        return "hello!!!!";
    }
}