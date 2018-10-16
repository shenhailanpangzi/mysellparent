//package com.lanpang.server.message;
//
//import org.springframework.cloud.stream.annotation.Input;
//import org.springframework.cloud.stream.annotation.Output;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.SubscribableChannel;
//
///**
// * @program: order
// * @description:定义输入通道和输出通道
// * @author: yanghao
// * @create: 2018-09-21 16:50
// **/
//public interface StreamClient {
//    public String INPUT = "msgInput";
//    public String OUTPUT = "msgOuput";
//
//    // 1. version 1
//    @Input(StreamClient.INPUT)
//    SubscribableChannel input();
//
//    @Output(StreamClient.INPUT)
//    MessageChannel output();
//
//    @Input(StreamClient.OUTPUT)
//    SubscribableChannel input2();
//
//    @Output(StreamClient.OUTPUT)
//    MessageChannel output2();
//    // 回答 回复消费
//
////    public String ACK_INPUT = "ackMsgInput";
////    public String ACK_OUTPUT = "ackMsgOuput";
////
////    @Input(StreamClient.ACK_INPUT)
////    SubscribableChannel ackInput();
////
////    @Output(StreamClient.ACK_OUTPUT)
////    MessageChannel ackOuput();
//}
