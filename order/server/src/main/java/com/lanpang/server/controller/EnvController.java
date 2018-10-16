package com.lanpang.server.controller;

import com.lanpang.server.config.GirlConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @program: order
 * @description:
 * @author: yanghao
 * @create: 2018-09-17 15:11
 **/
//加了这个注解的地方才能触发刷新配置
@RefreshScope
@RestController
@RequestMapping("/env")
public class EnvController {
    @Autowired
    private GirlConfig girlConfig;

    @Autowired
    private AmqpTemplate amqpTemplate;

//    @Autowired
//    private StreamClient streamClient;

    @Value("${env}")
    private String env;

    @GetMapping("/print")
    public String print() {
        return "env:" + env;
    }

    @GetMapping("/girl")
    public String girl() {
        return "name:" + girlConfig.getName() + "age:" + girlConfig.getAge();
    }

    /**
     * 使用amqpTemplate发送mq消息接口
     */
    @GetMapping("/send")
    public void send() {
        amqpTemplate.convertAndSend("myQueue2", "Now:" + System.currentTimeMillis());
    }

    /**
     * 使用amqpTemplate发送数码供应商服务消息接口
     */
    @GetMapping("/sendComputer")
    public void sendComputer() {
        amqpTemplate.convertAndSend("myOrder", "computer", "Now:" + System.currentTimeMillis());
    }

    /**
     * 使用amqpTemplate发送水果供应商消息接口
     */
    @GetMapping("/sendFruit")
    public void sendFruit() {
        amqpTemplate.convertAndSend("myOrder", "fruit", "Now:" + System.currentTimeMillis());
    }

    /**
     * 使用Stream发送消息
     */
//    @GetMapping("/myMessage1")
//    public void sendMessage() {
//        //获取当前时间
//        String message =LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        streamClient.output().send(MessageBuilder.withPayload(message).build());
//    }

}

