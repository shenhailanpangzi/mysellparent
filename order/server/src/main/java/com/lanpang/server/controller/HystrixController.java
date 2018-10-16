package com.lanpang.server.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * @program: order
 * @description: hystrix的控制器
 * @author: yanghao
 * @create: 2018-10-10 08:45
 **/
@RestController
//配置默认断路器
@DefaultProperties(defaultFallback = "defaultFallback")
public class HystrixController {
    @Autowired
    private LoadBalancerClient loadBalancerClient;


    //配置断路器 降级方法，超时时间配置
//    @HystrixCommand(fallbackMethod = "fallback",commandProperties = {
//            //HystrixCommandProperties 默认配置
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000")
//    })
//    服务熔断
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),//断路器 设置熔断
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),//设置在滚动时间窗中，断路器的最小请求数
            //断路器打开后，休眠时间窗开始计时，计时时间，这时降级逻辑成为主逻辑，当时间过期后断路器会重新进入半开状态，进行请求尝试，释放一部分请求到主逻辑进行判定
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "5000"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "40")//设置断路器打开的错误百分比条件
    })
//    @HystrixCommand
    @GetMapping("/getProductInfoList")
    public String getProductInfoList() {
        RestTemplate restTemplate = new RestTemplate();
        ServiceInstance serviceInstance = loadBalancerClient.choose("PRODUCT");
        String url = String.format("http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/product"+"/findByProductIdIn");
        String response = restTemplate.postForObject(url, Arrays.asList("123456", "123457"), String.class);
        return response;
    }


    private String fallback(){
        return "瞬间爆炸~太拥挤了~请稍后再试~~~~";
    }

    private String defaultFallback(){
        return "默认断路器~太拥挤了~请稍后再试~~~~";
    }


}
