package com.changgou.order.controller;

import com.changgou.core.AbstractCoreController;
import com.changgou.order.pojo.Order;
import com.changgou.order.service.OrderService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/order")
@CrossOrigin
public class OrderController extends AbstractCoreController<Order> {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        super(orderService, Order.class);
        this.orderService = orderService;
    }

    /**
     * 添加订单数据;
     * @param order
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Order order){
        Integer count = orderService.add(order);
        return new Result(true, StatusCode.OK,"ok",count);
    }
}
