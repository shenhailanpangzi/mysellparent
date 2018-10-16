package com.lanpang.server.service;


import com.lanpang.server.dto.OrderDTO;

/**
 * Created by 杨浩
 * 2018-06-11 18:23
 */
public interface OrderService {

    /**
     * 创建订单.
     */
    OrderDTO create(OrderDTO orderDTO);

    /** 查询单个订单. */
//    OrderDTO findOne(String orderId);
//
//    /** 查询某人订单列表. */
//    Page<OrderDTO> findList(String buyerOpenid, Pageable pageable);
//
//    /** 取消订单. */
//    OrderDTO cancel(OrderDTO orderDTO);
//
    /** 完结订单.只能卖家操作 */
    OrderDTO finish(String orderId);
//
//    /** 支付订单. */
//    OrderDTO paid(OrderDTO orderDTO);
//
//    /** 查询所有用户订单列表. */
//    Page<OrderDTO> findList(Pageable pageable);

}
