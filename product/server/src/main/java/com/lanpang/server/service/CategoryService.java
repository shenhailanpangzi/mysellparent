package com.lanpang.server.service;


import com.lanpang.server.dataobject.ProductCategory;

import java.util.List;

/**
 * 类目
 * Created by 杨浩
 * 2018-05-09 10:12
 */
public interface CategoryService {

    ProductCategory findOne(Integer categoryId);

    List<ProductCategory> findAll();

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    ProductCategory save(ProductCategory productCategory);
}
