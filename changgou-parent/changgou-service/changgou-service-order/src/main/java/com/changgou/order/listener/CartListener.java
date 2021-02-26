package com.changgou.order.listener;

import com.alibaba.fastjson.JSON;
import com.changgou.order.config.TokenDecode;
import com.changgou.order.dao.CartItemMapper;
import com.changgou.order.pojo.CartItem;
import com.changgou.order.pojo.MessageType;
import com.changgou.order.pojo.OrderItem;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

/**
 * @author pengmin
 * @date 2020/12/2 10:36
 */
@Component
public class CartListener {

    @Autowired
    private CartItemMapper cartItemMapper;

    @Autowired
    private TokenDecode tokenDecode;

    @RabbitListener(queues = "cart_queue")
    public void saveCart(String message){
        OrderItem orderItem = JSON.parseObject(message, OrderItem.class);
        if (orderItem != null){
            MessageType messageType = orderItem.getMessageType();
            // 添加订单;
            if (messageType != null && Objects.equals(messageType, MessageType.ADD)){
                CartItem cartItem = new CartItem();
                cartItem.setId(orderItem.getId());
                cartItem.setCartSkuId(orderItem.getSkuId());
                cartItem.setNum(orderItem.getNum());
                cartItem.setUsername(tokenDecode.getUserName());
                cartItem.setCreateTime(new Date());
                cartItem.setUpdateTime(cartItem.getCreateTime());
                cartItemMapper.insertSelective(cartItem);
            }else if (messageType != null && Objects.equals(messageType, MessageType.DELETE)){
                // 修改订单;(商品编号????,数量,更新时间等) 一般情况下商品ID不修改;
                CartItem cartItem = cartItemMapper.selectByPrimaryKey(orderItem.getId());
                //cartItem.setCartSkuId(orderItem.getSkuId());
                cartItem.setNum(orderItem.getNum());
                cartItem.setUpdateTime(new Date());
                cartItemMapper.updateByPrimaryKeySelective(cartItem);
            }else {
                //删除订单;
                cartItemMapper.deleteByPrimaryKey(orderItem.getId());
            }
        }
    }
}
