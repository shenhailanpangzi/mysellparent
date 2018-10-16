package com.lanpang.server.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @program: order
 * @description: 用来接收mq消息
 * @author: yanghao
 * @create: 2018-09-21 14:55
 **/
@Component
@Slf4j
public class MqReceiver {

    //需要手动在Rabbitmq中创建队列
//    @RabbitListener(queues = "myQueue")
//    public void process(String message){
//        log.info("MqReceiver-myQueue: {}",message);
//    }

    //    @RabbitListener(queuesToDeclare = @Queue ("myQueue2"))
    //自动创建队列 并且exchange和queue绑定
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("myQueue2"),
            exchange = @Exchange("myExchange2")
    ))
    public void processmyQueue2(String message) {
        log.info("MqReceiver-myQueue2: {}", message);
    }

    /**
     * 模拟数码供应商服务 接收消息
     *
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange("myOrder"),//交换机
            key = "computer",//routingkey 转发规则
            value = @Queue("computerOrder")
    ))
    public void processComputer(String message) {
        log.info("computer-MqReceiver: {}", message);
    }

    /**
     * 模拟水果供应商服务 接收消息
     *
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange("myOrder"),
            key = "fruit",
            value = @Queue("fruitOrder")
    ))
    public void processFruit(String message) {
        log.info("fruit-MqReceiver: {}", message);
    }
}
