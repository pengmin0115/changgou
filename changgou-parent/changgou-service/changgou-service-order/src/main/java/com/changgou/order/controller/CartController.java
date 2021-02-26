package com.changgou.order.controller;

import com.changgou.order.config.TokenDecode;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author pengmin
 * @date 2020/11/22 21:27
 */
@Controller
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private TokenDecode tokenDecode;

    /**
     * 添加购物车;
     * @param num
     * @param skuId
     * @return
     */
    @RequestMapping("/add")
    public Result add(Integer num, Long skuId){
        // 从token令牌-载荷中 解析出来用户名;
        String username = tokenDecode.getUserName();
        cartService.add(num,skuId,username);
        return new Result(true, StatusCode.OK,"ok");
    }

    /**
     * 获取购物车列表数据;
     * @return
     */
    @GetMapping("/list")
    public Result list(){
        String username = tokenDecode.getUserName();
        List<OrderItem> orderItemList = cartService.list(username);
        return new Result(true, StatusCode.OK,"ok",orderItemList);
    }
}
