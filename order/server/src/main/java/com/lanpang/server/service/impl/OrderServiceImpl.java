package com.lanpang.server.service.impl;


import com.lanpang.client.ProductClient;
import com.lanpang.common.DecreaseStockInput;
import com.lanpang.common.ProductInfoOutput;
import com.lanpang.server.dataobject.OrderDetail;
import com.lanpang.server.dataobject.OrderMaster;
import com.lanpang.server.dto.OrderDTO;
import com.lanpang.server.enums.OrderStatusEnum;
import com.lanpang.server.enums.PayStatusEnum;
import com.lanpang.server.enums.ResultEnum;
import com.lanpang.server.exception.SellException;
import com.lanpang.server.repository.OrderDetailRepository;
import com.lanpang.server.repository.OrderMasterRepository;
import com.lanpang.server.service.OrderService;
import com.lanpang.server.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 订单
 * Created by 杨浩
 * 2018-06-11 18:43
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Autowired
    private ProductClient productClient;

    /**
     * 创建订单.
     */
    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        String orderId = KeyUtil.genUniqueKey();

        //1. 查询商品（数量, 价格）查询所有上架商品是否存在
        List<String> productIdList = orderDTO.getOrderDetailList().stream()
                .map(e -> e.getProductId())
                .collect(Collectors.toList());
//        List<String> productIdList = orderDTO.getOrderDetailList().stream()
//                .map(OrderDetail::getProductId)
//                .collect(Collectors.toList());
        //查询商品信息(调用商品服务)
        List<ProductInfoOutput> productInfoList = productClient.findByProductIdIn(productIdList);
//        秒杀场景查询商品 start。。。
        //1、读redis中的库存，库存保存在redis中
        //2、通过redis库存判断库存是否充足，减库存并将新值重新设置进redis，多线程并发情况下加分布式锁
        //3、订单服务数据写入数据库，发送消息返回结果
        //4、订单入库异常，手动回滚redis数据
//        秒杀场景查询商品 end。。。
        //2、计算总价 默认总价为0
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        for (OrderDetail orderDetail : orderDTO.getOrderDetailList()) {
            for (ProductInfoOutput productInfo : productInfoList) {
                //判断商品是否存在
                if (productInfo.getProductId().equals(orderDetail.getProductId())) {
                    //2. 计算订单总价
                    orderAmount = productInfo.getProductPrice()
                            .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                            .add(orderAmount);
                    //订单详情入库
                    orderDetail.setDetailId(KeyUtil.genUniqueKey());
                    orderDetail.setOrderId(orderId);
                    //将productInfo拷贝到orderDetail 属性值是null也会被拷贝
                    BeanUtils.copyProperties(productInfo, orderDetail);
                    orderDetailRepository.save(orderDetail);
                }
            }
        }

        //3. 异步扣库存
        List<DecreaseStockInput> decreaseStockInputs = orderDTO.getOrderDetailList().stream()
                .map(e -> new DecreaseStockInput(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productClient.decreaseStock(decreaseStockInputs);

        //4. 写入订单数据库（orderMaster和orderDetail）
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);

        return orderDTO;
    }

//    /** 查询单个订单. */
//    @Override
//    public OrderDTO findOne(String orderId) {
//        OrderMaster orderMaster = orderMasterRepository.findById(orderId).get();
//        if (orderMaster == null) {
//            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
//        }
//        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
//        if (CollectionUtils.isEmpty(orderDetailList)) {
//            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
//        }
//        OrderDTO orderDTO = new OrderDTO();
//        BeanUtils.copyProperties(orderMaster, orderDTO);
//        orderDTO.setOrderDetailList(orderDetailList);
//        return orderDTO;
//    }
//    /** 查询用户订单列表. */
//    @Override
//    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
//        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenid, pageable);
//
//        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());
//
//        return new PageImpl<OrderDTO>(orderDTOList, pageable, orderMasterPage.getTotalElements());
//    }
    /** 完结订单. */
    @Override
    @Transactional
    public OrderDTO finish(String orderId) {
        //1、查询订单
        Optional<OrderMaster> orderMasterOptional = orderMasterRepository.findById(orderId);
        //2、判断订单是否存在
        if (!orderMasterOptional.isPresent()){
            log.error("【完结订单】订单不存在, orderId={}", orderMasterOptional.get().getOrderId());
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        //3、判断订单状态 是否是新订单
        if (orderMasterOptional.get().getOrderStatus()!=OrderStatusEnum.NEW.getCode()){
            log.error("【完结订单】订单状态不正确, orderId={}, OrderStatus={}", orderMasterOptional.get().getOrderId(),orderMasterOptional.get().getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //4、修改订单状态
        orderMasterOptional.get().setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster updateResult = orderMasterRepository.save(orderMasterOptional.get());
        if (updateResult == null) {
            log.error("【完结订单】更新失败, orderMaster={}", orderMasterOptional.get());
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        //5、组装反参
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        BeanUtils.copyProperties(orderMasterOptional.get(), orderDTO);
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetails)){
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }
        orderDTO.setOrderDetailList(orderDetails);
        //推送微信模版消息
//        pushMessageService.orderStatus(orderDTO);
        return orderDTO;
    }
//    /** 取消订单. */
//    @Override
//    @Transactional
//    public OrderDTO cancel(OrderDTO orderDTO) {
//        OrderMaster orderMaster = new OrderMaster();
//
//        //1、判断订单状态
//        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
//            log.error("【取消订单】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
//            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
//        }
//
//        //2、修改订单状态
//        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
//        BeanUtils.copyProperties(orderDTO, orderMaster);
//        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
//        if (updateResult == null) {
//            log.error("【取消订单】更新失败, orderMaster={}", orderMaster);
//            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
//        }
//
//        //3、返回库存
//        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
//            log.error("【取消订单】订单中无商品详情, orderDTO={}", orderDTO);
//            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
//        }
//        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
//                .map(e -> new CartDTO(e.getProductId(), e.getProductQuantity()))
//                .collect(Collectors.toList());
//        productService.increaseStock(cartDTOList);
//
//        //4、如果已支付, 需要退款
//        if (orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
////            payService.refund(orderDTO);
//        }
//        return orderDTO;
//    }

//    /** 支付订单. */
//    @Override
//    @Transactional
//    public OrderDTO paid(OrderDTO orderDTO) {
//        //判断订单状态
//        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
//            log.error("【订单支付完成】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
//            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
//        }
//
//        //判断支付状态
//        if (!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
//            log.error("【订单支付完成】订单支付状态不正确, orderDTO={}", orderDTO);
//            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
//        }
//
//        //修改支付状态
//        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
//        OrderMaster orderMaster = new OrderMaster();
//        BeanUtils.copyProperties(orderDTO, orderMaster);
//        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
//        if (updateResult == null) {
//            log.error("【订单支付完成】更新失败, orderMaster={}", orderMaster);
//            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
//        }
//
//        return orderDTO;    }
//    /** 查询订单列表. */
//    @Override
//    public Page<OrderDTO> findList(Pageable pageable) {
//        Page<OrderMaster> orderMasterPage = orderMasterRepository.findAll(pageable);
//
//        List<OrderDTO> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());
//
//        return new PageImpl<>(orderDTOList, pageable, orderMasterPage.getTotalElements());
//    }
}
