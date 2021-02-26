package com.changgou.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.order.pojo.MessageType;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import entity.Result;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author pengmin
 * @date 2020/11/22 21:38
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SpuFeign spuFeign;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void add(Integer num, Long skuId, String username) {
        if (num <= 0) {
            //删除购物车中的数据;
            redisTemplate.boundHashOps("Cart_" + username).delete(skuId);
        }
        // 获取sku/spu;
        Result<Sku> skuRes = skuFeign.findById(skuId);
        Sku sku = skuRes.getData();
        Result<Spu> spuRes = spuFeign.findById(sku.getSpuId());
        Spu spu = spuRes.getData();
        // 设置orderItem信息;
        OrderItem orderItem = new OrderItem();
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());
        orderItem.setSpuId(spu.getId());
        orderItem.setSkuId(sku.getId());
        orderItem.setName(sku.getName());
        orderItem.setPrice(sku.getPrice());
        orderItem.setNum(num);
        orderItem.setMoney(num * sku.getPrice());
        orderItem.setPayMoney(num * sku.getPrice());
        orderItem.setImage(sku.getImage());
        // 将数据存储到redis中;
        redisTemplate.boundHashOps("Cart_" + username).put(skuId, orderItem);
        redisTemplate.boundHashOps("Cart_" + username).expire(7, TimeUnit.DAYS);
        //发送消息,存入订单数据;
        orderItem.setMessageType(MessageType.ADD);
        rabbitTemplate.convertAndSend("cart_exchange","", JSON.toJSONString(orderItem));
    }

    @Override
    public List<OrderItem> list(String username) {
        List<OrderItem> list = redisTemplate.boundHashOps("Cart_" + username).values();
        return list;
    }
}
