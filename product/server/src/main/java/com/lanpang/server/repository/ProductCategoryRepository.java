package com.lanpang.server.repository;

import com.lanpang.server.dataobject.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by 杨浩
 * 2018-05-07 14:35
 */
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    //使用这种查询方式的实体必须由一个无参构造方法
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    List<ProductCategory> findAll();
}
