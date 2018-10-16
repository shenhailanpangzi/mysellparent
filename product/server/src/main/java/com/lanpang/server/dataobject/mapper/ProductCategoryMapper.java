package com.lanpang.server.dataobject.mapper;//package com.lanpang.product.dataobject.mapper;
//
//import com.lanpang.sell.dataobject.ProductCategory;
//import org.apache.ibatis.annotations.*;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * 类目
// * Created by 杨浩
// * 2018-05-07 14:30
// */
//
//public interface ProductCategoryMapper {
//
//    //    通过参数插入
//    @Insert("insert into product_category(category_name,category_type) values(#{category_name, jdbcType=VARCHAR},#{category_Type, jdbcType=INTEGER})")
//    int insertByMap(Map<String, Object> map);
//
//    //    通过对象插入
//    @Insert("insert into product_category(category_name,category_type) values(#{categoryName, jdbcType=VARCHAR},#{categoryType, jdbcType=INTEGER})")
//    int insertByObject(ProductCategory category);
//
//    //    单返回值 通过字段查询
//    @Select("select * from product_category where category_type = #{categoryType}")
//    //设置返回参数中的参数
//    @Results({
//            @Result(column = "category_id", property = "categoryId"),
//            @Result(column = "category_type", property = "categoryType"),
//            @Result(column = "category_name", property = "categoryName")
//    })
//    ProductCategory findByCategoryType(Integer categoryType);
//
//    //    多返回值 模糊查询
//    @Select("select * from product_category where category_name like CONCAT(CONCAT('%',#{categoryName},'%'))")
//    //设置返回参数中的参数
//    @Results({
//            @Result(column = "category_id", property = "categoryId"),
//            @Result(column = "category_type", property = "categoryType"),
//            @Result(column = "category_name", property = "categoryName")
//    })
//    List<ProductCategory> findByCategoryName(String categoryName);
//
//    //    根据字段更新
//    @Update("update product_category set category_name = #{categoryName} where category_type = #{categoryType}")
//    int updateByCategoryType(@Param("categoryName") String categoryName, @Param("categoryType") Integer categoryType);
//
//    //    通过对象插入
//    @Update("update product_category set category_name = #{categoryName} where category_type = #{categoryType}")
//    int updateByObject(ProductCategory category);
//
//    //    通过对象插入
//    @Delete("delete product_category where category_type = #{categoryType}")
//    int deleteByCategoryType(Integer categoryType);
//
//    //      通过xml方式查询
//    ProductCategory selectByCategoryType(Integer categoryType);
//
//}
