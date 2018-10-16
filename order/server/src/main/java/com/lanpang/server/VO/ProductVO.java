package com.lanpang.server.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 商品(包含类目)
 * Created by 杨浩
 * 2018-05-12 14:20
 */
@Data
public class ProductVO {

    @JsonProperty("name")//给前端的名字
    private String categoryName;

    @JsonProperty("type")
    private Integer categoryType;

    @JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;
}
