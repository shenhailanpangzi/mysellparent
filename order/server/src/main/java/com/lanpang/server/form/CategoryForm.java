package com.lanpang.server.form;

import lombok.Data;

/**
 * Created by 杨浩
 * 2018-07-23 21:43
 */
@Data
public class CategoryForm {

    private Integer categoryId;

    /**
     * 类目名字.
     */
    private String categoryName;

    /**
     * 类目编号.
     */
    private Integer categoryType;
}
