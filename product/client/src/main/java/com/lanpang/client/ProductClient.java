package com.lanpang.client;

import com.lanpang.common.DecreaseStockInput;
import com.lanpang.common.ProductInfoOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @program: order
 * @description: feign要调用的接口
 * @author: yanghao
 * @create: 2018-09-12 10:31
 **/
@FeignClient(name = "product", fallback = ProductClient.ProductClientFallback.class)
public interface ProductClient {

    @GetMapping("/msg")
    String productMsg();

    @GetMapping("/getFProductMsg")
    String getFProductMsg();

    @PostMapping("/product/findByProductIdIn")
    List<ProductInfoOutput> findByProductIdIn(@RequestBody List<String> productIdList);

    @GetMapping("product/decreaseStockProcess")
    void decreaseStock(List<DecreaseStockInput> decreaseStockInputs);

    @GetMapping("product/increaseStock")
    void increaseStock(List<DecreaseStockInput> decreaseStockInputs);

    /**
     * 如果产生服务降级就会就如这个类
     */
    @Component
    static class ProductClientFallback implements ProductClient{

        @Override
        public String productMsg() {
            System.out.println("productMsg 方法 进入熔断器~~");
            return "productMsg 方法 进入熔断器~~";
        }

        @Override
        public String getFProductMsg() {
            return "getFProductMsg 方法 进入熔断器~~";
        }

        @Override
        public List<ProductInfoOutput> findByProductIdIn(List<String> productIdList) {
            System.out.println("findByProductIdIn 方法 进入熔断器~~");

            return null;
        }

        @Override
        public void decreaseStock(List<DecreaseStockInput> decreaseStockInputs) {
            System.out.println("decreaseStock 方法 进入熔断器~~");

        }

        @Override
        public void increaseStock(List<DecreaseStockInput> decreaseStockInputs) {
            System.out.println("increaseStock 方法 进入熔断器~~");

        }
    }
}

