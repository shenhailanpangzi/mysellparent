package com.lanpang.server.service;


import com.lanpang.server.dataobject.ProductInfo;
import com.lanpang.server.dto.CartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 商品
 * Created by 杨浩
 * 2018-05-09 17:27
 */
public interface ProductService {

    ProductInfo findOne(String productId);

    /**
     * 查询所有在架商品列表
     *
     * @return
     */
    List<ProductInfo> findUpAll();

    List<ProductInfo> findByProductIdIn(List<String> ProductIdList);

    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo save(ProductInfo productInfo);

    //加库存
    void increaseStock(List<CartDTO> cartDTOList);

    //发送扣库存mq消息
    void decreaseStockProcess(List<CartDTO> cartDTOList);

    //上架
    ProductInfo onSale(String productId);

    //下架
    ProductInfo offSale(String productId);
}
