package com.lanpang.server.controller;

import com.lanpang.server.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: 供client调用的controller
 * @description:
 * @author: yanghao
 * @create: 2018-09-11 17:05
 **/
@RestController
public class ServerController {

    @Autowired
    private ProductService productService;

    @GetMapping("/msg")
    public String msg(HttpServletRequest request) {
        return "this is a product msg!!!" + request.getRemoteHost() + request.getServerPort();
    }


}
