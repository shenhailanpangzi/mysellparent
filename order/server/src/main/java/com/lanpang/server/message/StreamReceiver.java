//package com.lanpang.server.message;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.stream.annotation.EnableBinding;
//import org.springframework.cloud.stream.annotation.StreamListener;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Component;
//
///**
// * @program: order
// * @description: 消息接收端
// * @author: yanghao
// * @create: 2018-09-21 17:13
// **/
//@Component
//@EnableBinding(StreamClient.class)//定义的接口
//@Slf4j
//public class StreamReceiver {
//
//    /**
//     *接收myMessage队列消息
//     */
//    // 1. version 1
//    @StreamListener(StreamClient.INPUT)
////    @SendTo(StreamClient.ACK_OUTPUT)
//    public void process(String message) {
//        log.info("接收到了数据: {}", message);
////        return "确认被消费";
//    }
//
//    // 1. version 1
////    @StreamListener(StreamClient.INPUT)
////    @SendTo(StreamClient.ACK_OUTPUT)
////    public void process(String message) {
////        log.info("接收到了数据: {}", message);
////        return "确认被消费";
////    }
////
////    @StreamListener(StreamClient.ACK_INPUT)
////    public void ackProcess(String message) {
////        log.info("接收到了确认消费的消息: {}", message);
////    }
//
//}
