package com.lanpang.server.controller;

import com.lanpang.server.VO.ResultVO;
import com.lanpang.server.converter.OrderForm2OrderDTOConverter;
import com.lanpang.server.dto.OrderDTO;
import com.lanpang.server.enums.ResultEnum;
import com.lanpang.server.exception.SellException;
import com.lanpang.server.form.OrderForm;
import com.lanpang.server.service.OrderService;
import com.lanpang.server.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 买家端 订单  Controller层数据展现
 * Created by 杨浩
 * 2018-05-12 14:08
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/hello")
    public String hello(){
        return "hello!!!!";
    }

    //创建订单
    @PostMapping("/create")
    public ResultVO<Map<String, String>> create(@Valid OrderForm orderForm,
//   如果在使用接口返回信息的时候，可以直接拿到bindingResult中的错误信息
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("【创建订单】参数不正确, orderForm={}", orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }

        OrderDTO orderDTO = OrderForm2OrderDTOConverter.convert(orderForm);
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("【创建订单】购物车不能为空");
            throw new SellException(ResultEnum.CART_EMPTY);
        }

        OrderDTO createResult = orderService.create(orderDTO);

        Map<String, String> map = new HashMap<>();
        map.put("orderId", createResult.getOrderId());

        return ResultVOUtil.success(map);
    }


//    //订单列表
//    @GetMapping("/list")
//    public ResultVO<List<OrderDTO>> list(@RequestParam("openid") String openid,
//                                         @RequestParam(value = "page", defaultValue = "0") Integer page,
//                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
//        if (StringUtils.isEmpty(openid)) {
//            log.error("【查询订单列表】openid为空");
//            throw new SellException(ResultEnum.PARAM_ERROR);
//        }
//        PageRequest request = new PageRequest(page, size);
//        Page<OrderDTO> orderDTOPage = orderService.findList(openid, request);
//
//        return ResultVOUtil.success(orderDTOPage.getContent());
//    }
//
//
//    //订单详情
//    @GetMapping("/detail")
//    public ResultVO<OrderDTO> detail(@RequestParam("openid") String openid,
//                                     @RequestParam("orderId") String orderId) {
//        if (StringUtils.isEmpty(openid)&&StringUtils.isEmpty(orderId)) {
//            log.error("【查询订单列表】openid为空");
//            throw new SellException(ResultEnum.PARAM_ERROR);
//        }
//        OrderDTO orderDTO = buyerService.findOrderOne(openid,orderId);
//        return ResultVOUtil.success(orderDTO);
//    }
//
//    //取消订单
//    @PostMapping("/cancel")
//    public ResultVO cancel(@RequestParam("openid") String openid,
//                           @RequestParam("orderId") String orderId) {
//        buyerService.cancelOrder(openid, orderId);
//        return ResultVOUtil.success();
//    }
    /**
     * 完结订单
     */
    @GetMapping("/finish")
    public ResultVO<OrderDTO> finish(@RequestParam("orderId") String orderId){

        return ResultVOUtil.success(orderService.finish(orderId));
    }
}
