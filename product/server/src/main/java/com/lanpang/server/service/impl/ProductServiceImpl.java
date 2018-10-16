package com.lanpang.server.service.impl;


import com.lanpang.common.ProductInfoOutput;
import com.lanpang.server.dataobject.ProductInfo;
import com.lanpang.server.dto.CartDTO;
import com.lanpang.server.enums.ProductStatusEnum;
import com.lanpang.server.enums.ResultEnum;
import com.lanpang.server.exception.SellException;
import com.lanpang.server.repository.ProductInfoRepository;
import com.lanpang.server.service.ProductService;
import com.lanpang.server.utils.JsonUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by 杨浩
 * 2018-05-09 17:31
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductInfoRepository repository;
    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public ProductInfo findOne(String productId) {
        return repository.findById(productId).get();
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return repository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public List<ProductInfo> findByProductIdIn(List<String> ProductIdList) {
        return repository.findByProductIdIn(ProductIdList);
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }

    //加库存
    @Override
    @Transactional
    public void increaseStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO : cartDTOList) {
            Optional<ProductInfo> productInfo = repository.findById(cartDTO.getProductId());
            if (!productInfo.isPresent()) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            Integer result = productInfo.get().getProductStock() + cartDTO.getProductQuantity();
            productInfo.get().setProductStock(result);

            repository.save(productInfo.get());
        }

    }
    //减库存-发送mq消息
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void decreaseStockProcess(List<CartDTO> cartDTOList) {
        List<ProductInfo> productInfos = decreaseStock(cartDTOList);

        List<ProductInfoOutput> outputs = productInfos.stream().map(e -> {
            ProductInfoOutput output = new ProductInfoOutput();
            BeanUtils.copyProperties(e, output);
            return output;
        }).collect(Collectors.toList());
        //发送mq消息 扣库存
        amqpTemplate.convertAndSend("productInfo", JsonUtil.toJson(outputs));
        System.out.println("放入消息队列:" + JsonUtil.toJson(outputs));
    }

    //减库存
    public List<ProductInfo> decreaseStock(List<CartDTO> cartDTOList) {
        List<ProductInfo> productInfos = new ArrayList<>();
        for (CartDTO cartDTO : cartDTOList) {
            Optional<ProductInfo> productInfo = repository.findById(cartDTO.getProductId());
            if (!productInfo.isPresent()) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer result = productInfo.get().getProductStock() - cartDTO.getProductQuantity();
            if (result < 0) {
                throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            }
            productInfo.get().setProductStock(result);
            repository.save(productInfo.get());
            productInfos.add(productInfo.get());
        }
        return productInfos;
    }



    //上架
    @Override
    public ProductInfo onSale(String productId) {
        Optional<ProductInfo> productInfo = repository.findById(productId);
        if (!productInfo.isPresent()) {
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        if (productInfo.get().getProductStatusEnum() == ProductStatusEnum.UP) {
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }
        //更新
        productInfo.get().setProductStatus(ProductStatusEnum.UP.getCode());
        return repository.save(productInfo.get());
    }

    //下架
    @Override
    public ProductInfo offSale(String productId) {
        Optional<ProductInfo> productInfo = repository.findById(productId);
        if (!productInfo.isPresent()) {
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        if (productInfo.get().getProductStatusEnum() == ProductStatusEnum.DOWN) {
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }

        //更新
        productInfo.get().setProductStatus(ProductStatusEnum.DOWN.getCode());
        return repository.save(productInfo.get());
    }

}
