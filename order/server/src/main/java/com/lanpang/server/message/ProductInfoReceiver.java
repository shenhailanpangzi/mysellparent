package com.lanpang.server.message;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lanpang.common.ProductInfoOutput;
import com.lanpang.server.VO.ProductInfoVO;
import com.lanpang.server.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: order
 * @description: 商品的mq
 * @author: yanghao
 * @create: 2018-09-25 11:05
 **/
@Component
@Slf4j
public class ProductInfoReceiver {

    private static final String PRODUCT_STOCK_TEMPLATE = "product_stock_%s";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("productInfo"),
            exchange = @Exchange("productInfo")
    ))
    public void processmyQueue2(String message) {
        //message ==> productInfoOutput
        List<ProductInfoOutput> outputs = (List<ProductInfoOutput>) JsonUtil.fromJson(message,
                new TypeReference<List<ProductInfoOutput>>() {
                });
        log.info("从队列{productInfo}接收到消息: {}", outputs);
        //储存到redis
        outputs.forEach(e -> {
            redisTemplate.opsForValue().set(String.format(PRODUCT_STOCK_TEMPLATE, e.getProductId()),
                    String.valueOf(e.getProductStock()));
        });
    }

}
