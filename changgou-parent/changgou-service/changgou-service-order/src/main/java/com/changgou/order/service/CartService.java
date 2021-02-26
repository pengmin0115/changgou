package com.changgou.order.service;

import com.changgou.order.pojo.OrderItem;

import java.util.List;

/**
 * @author pengmin
 * @date 2020/11/22 21:34
 */

public interface CartService {

    void add(Integer num, Long skuId, String username);

    List<OrderItem> list(String username);
}
