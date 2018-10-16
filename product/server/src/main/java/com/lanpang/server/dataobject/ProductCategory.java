package com.lanpang.server.dataobject;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 类目
 * Created by 杨浩
 * 2018-05-07 14:30
 */
@Entity
//使用lombok插件 包括getset等
@Data
//动态更新时间
@DynamicUpdate
@DynamicInsert
public class ProductCategory {

    /**
     * 类目id.
     */
    @Id
    @GeneratedValue //自增注解
    private Integer categoryId;

    /**
     * 类目名字.
     */
    private String categoryName;

    /**
     * 类目编号.
     */
    private Integer categoryType;

    private Date createTime;

    private Date updateTime;

    public ProductCategory() {
    }

    public ProductCategory(String categoryName, Integer categoryType) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }

}
