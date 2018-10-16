package com.lanpang.server.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lanpang.server.dataobject.OrderDetail;
import com.lanpang.server.enums.OrderStatusEnum;
import com.lanpang.server.enums.PayStatusEnum;
import com.lanpang.server.utils.EnumUtil;
import com.lanpang.server.utils.serializer.Date2LongSerializer;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单对象
 * Created by 杨浩
 * 2018-06-11 18:30
 */
@Data
@ToString
@DynamicUpdate  //更新时动态插入时间
@DynamicInsert  //新增时动态插入时间
//@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)//旧用法
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {

    /**
     * 订单id.
     */
    private String orderId;

    /**
     * 买家名字.
     */
    private String buyerName;

    /**
     * 买家手机号.
     */
    private String buyerPhone;

    /**
     * 买家地址.
     */
    private String buyerAddress;

    /**
     * 买家微信Openid.
     */
    private String buyerOpenid;

    /**
     * 订单总金额.
     */
    private BigDecimal orderAmount;

    /**
     * 订单状态, 默认为0新下单.
     */
    private Integer orderStatus;

    /**
     * 支付状态, 默认为0未支付.
     */
    private Integer payStatus;

    /**
     * 创建时间.
     */
    @JsonSerialize(using = Date2LongSerializer.class)//
    private Date createTime;

    /**
     * 更新时间.
     */
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;

    //订单详情列表
    List<OrderDetail> orderDetailList;

    @JsonIgnore
    public OrderStatusEnum getOrderStatusEnum() {
        return EnumUtil.getByCode(orderStatus, OrderStatusEnum.class);
    }

    @JsonIgnore
    public PayStatusEnum getPayStatusEnum() {
        return EnumUtil.getByCode(payStatus, PayStatusEnum.class);
    }

}
