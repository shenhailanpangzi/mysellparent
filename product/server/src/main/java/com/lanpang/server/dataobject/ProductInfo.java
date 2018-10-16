package com.lanpang.server.dataobject;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lanpang.server.enums.ProductStatusEnum;
import com.lanpang.server.utils.EnumUtil;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品
 * Created by 杨浩
 * 2018-05-09 11:30
 */
@Entity
@Data
@DynamicUpdate  //更新时动态插入时间
@DynamicInsert  //新增时动态插入时间
public class ProductInfo {

    @Id
    private String productId;

    /**
     * 名字.
     */
    private String productName;

    /**
     * 单价.
     */
    private BigDecimal productPrice;

    /**
     * 库存.
     */
    private Integer productStock;

    /**
     * 描述.
     */
    private String productDescription;

    /**
     * 小图.
     */
    private String productIcon;

    /**
     * 状态, 0正常1下架.
     */
    private Integer productStatus = ProductStatusEnum.UP.getCode();

    /**
     * 类目编号.
     */
    private Integer categoryType;

    private Date createTime;

    private Date updateTime;

    /**
     * 作用：在json序列化时将java bean中的一些属性忽略掉，序列化和反序列化都受影响。
     * 使用方法：一般标记在属性或者方法上，返回的json数据即不包含该属性。
     */
    @JsonIgnore
    public ProductStatusEnum getProductStatusEnum() {
        return EnumUtil.getByCode(productStatus, ProductStatusEnum.class);
    }
}
