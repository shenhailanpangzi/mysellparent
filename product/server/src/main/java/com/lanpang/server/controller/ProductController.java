package com.lanpang.server.controller;


import com.lanpang.server.VO.ProductInfoVO;
import com.lanpang.server.VO.ProductVO;
import com.lanpang.server.VO.ResultVO;
import com.lanpang.server.dataobject.ProductCategory;
import com.lanpang.server.dataobject.ProductInfo;
import com.lanpang.server.dto.CartDTO;
import com.lanpang.server.service.CategoryService;
import com.lanpang.server.service.ProductService;
import com.lanpang.server.utils.ResultVOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 买家端 商品  Controller层数据展现
 * Created by 杨浩
 * 2018-05-12 14:08
 */
//返回json不会跳转
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/list")
    //allowCredentials = "true" 标识允许cookie跨域
    @CrossOrigin(allowCredentials = "true")
    public ResultVO list() {
        //1. 查询所有的上架商品
        List<ProductInfo> productInfoList = productService.findUpAll();

        //2. 查询类目(一次性查询)
//        List<Integer> categoryTypeList = new ArrayList<>();
        //传统方法
//        for (ProductInfo productInfo : productInfoList) {
//            categoryTypeList.add(productInfo.getCategoryType());
//        }

        //精简方法(java8, lambda)
//        List<Integer> categoryTypeList = productInfoList.stream()
//                .map(e -> e.getCategoryType())
//                .collect(Collectors.toList());
        //最简方法
        List<Integer> categoryTypeList =productInfoList.stream().map(ProductInfo::getCategoryType).collect(Collectors.<Integer>toList());
//                productInfoList.stream()
//                .map(ProductInfo::getCategoryType)
//                .collect(Collectors.toList());

        List<ProductCategory> productCategoryList = categoryService.findByCategoryTypeIn(categoryTypeList);

        //3. 数据拼装
        List<ProductVO> productVOList = new ArrayList<ProductVO>();
        for (ProductCategory productCategory : productCategoryList) {
            ProductVO productVO = new ProductVO();
            productVO.setCategoryType(productCategory.getCategoryType());
            productVO.setCategoryName(productCategory.getCategoryName());

            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for (ProductInfo productInfo : productInfoList) {
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())) {
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    //Spring提供的BeanUtils  可以把一个对象属性的值拷贝到另一个对象
                    BeanUtils.copyProperties(productInfo, productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }
        return ResultVOUtil.success(productVOList);
    }

    @PostMapping("/findByProductIdIn")
    public List<ProductInfo> findByProductIdIn(@RequestBody List<String> productIdList) {
//        测试熔断器代码start。。。
//        try {
//        boolean a =new Random().nextBoolean();
//        if (a){
//            Thread.sleep(2000);
//            a=false;
//        }else {
//            Thread.sleep(1000);
//            a=true;
//        }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        测试熔断器代码end。。。
        return productService.findByProductIdIn(productIdList);
    }

    /**
     * 异步扣库存
     *
     * @param cartDTOList
     * @return
     */
    @PostMapping("/decreaseStockProcess")
    public void decreaseStock(@RequestBody List<CartDTO> cartDTOList) {
        productService.decreaseStockProcess(cartDTOList);
    }

    /**
     * 加库存
     *
     * @param cartDTOList
     * @return
     */
    @PostMapping("/increaseStock")
    public void increaseStock(@RequestBody List<CartDTO> cartDTOList) {
        productService.increaseStock(cartDTOList);
    }
}
