package com.lanpang.server.controller;


import com.lanpang.client.ProductClient;
import com.lanpang.common.DecreaseStockInput;
import com.lanpang.common.ProductInfoOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * @program: order
 * @description: 调用服务端的演示
 * @author: yanghao
 * @create: 2018-09-11 17:07
 **/
@RestController
@Slf4j
public class ClientController {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ProductClient productClient;

    /**
     * 通过restTemplate链接服务端
     *
     * @return
     */
    @GetMapping("/getProductMsg")
    public String getProductMsg() {
//      //1、第一种方式 直接使用 restTemplate 缺点（url写死，）
//        RestTemplate restTemplate = new RestTemplate();
//        String response = restTemplate.getForObject("http://localhost:8091/msg/",String.class);
//        log.info("response={}",response);
        //2.第二种方式 利用loadBalancerClient 通过应用名获取url 然后再使用resttemplate
//            选择调用地址
        ServiceInstance serviceInstance = loadBalancerClient.choose("PRODUCT");
        String url = String.format("http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/msg");
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(url, String.class);
        //3.第三种方式 使用@LoadBalanced注解 配置bean方式 可以在url中使用应用名字
//        String response = restTemplate.getForObject("http://PRODUCT/msg/",String.class);
        log.info("response={}", response);
        return response;
    }

    /**
     * 通过Feign调用
     */
    @GetMapping("/getFProductMsg")
    public String getFProductMsg() {
        String response = productClient.productMsg();
        log.info("response={}", response);
        return response;
    }

    /**
     * 通过Feign调用
     */
    @GetMapping("/findByProductIdIn")
    public List<ProductInfoOutput> findByProductIdIn() {
        List<String> productIdList = Arrays.asList("123456", "1234568");
        List<ProductInfoOutput> productInfoList = productClient.findByProductIdIn(productIdList);
        log.info("response={}", productInfoList);
        return productInfoList;
    }

    @GetMapping("/decreaseStock")
    public String decreaseStock() {
        List<DecreaseStockInput> cartDTOList = Arrays.asList(
                new DecreaseStockInput("123456", 2),
                new DecreaseStockInput("1234568", 2));
        productClient.decreaseStock(cartDTOList);
        return "ok";
    }

    @GetMapping("/increaseStock")
    public String increaseStock() {
        List<DecreaseStockInput> cartDTOList = Arrays.asList(
                new DecreaseStockInput("123456", 2),
                new DecreaseStockInput("1234568", 2));
        productClient.increaseStock(cartDTOList);
        return "ok";
    }
}
