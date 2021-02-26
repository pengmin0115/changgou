package com.changgou.order.service.impl;

import com.changgou.core.service.impl.CoreServiceImpl;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.order.config.TokenDecode;
import com.changgou.order.dao.OrderItemMapper;
import com.changgou.order.dao.OrderMapper;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.OrderService;
import com.changgou.order.util.IdWorker;
import com.changgou.user.feign.UserFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/****
 * @Author:admin
 * @Description:Order业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class OrderServiceImpl extends CoreServiceImpl<Order> implements OrderService {

    private OrderMapper orderMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SkuFeign skuFeign;
    
    @Autowired
    private UserFeign userFeign;

    @Autowired
    private TokenDecode tokenDecode;
    
    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper) {
        super(orderMapper, Order.class);
        this.orderMapper = orderMapper;
    }

    @Override
    //@GlobalTransactional //开启seata的分布式事务;
    public Integer add(Order order) {
        //设置订单属性;
        String id = idWorker.nextId() + "";
        order.setId(id);
        order.setUsername(tokenDecode.getUserName());
        Integer totalNum = 0;
        Integer totalMoney = 0;
        List<OrderItem> list = redisTemplate.boundHashOps("Cart_" + order.getUsername()).values();
        for (OrderItem orderItem : list) {
            Integer num = orderItem.getNum();
            Integer money = orderItem.getMoney();
            totalNum += num;
            totalMoney += money;
            //设置订单详情属性;
            String itemId = idWorker.nextId() + "";
            orderItem.setId(itemId);
            orderItem.setOrderId(id);
            orderItem.setIsReturn("0");
            // 将订单详情数据落库;
            orderItemMapper.insertSelective(orderItem);
            // todo 下单减SKU库存数量; 实现批量修改库存数据?
            skuFeign.deCount(orderItem.getSkuId(),num);
        }
        //todo 调用积分微服务加积分;
        userFeign.addPoints(order.getUsername(),10);
        order.setTotalNum(totalNum);
        order.setTotalMoney(totalMoney);
        order.setPayMoney(totalMoney);
        order.setCreateTime(new Date());
        order.setUpdateTime(order.getCreateTime());
        order.setBuyerRate("0");
        order.setOrderStatus("0");
        order.setPayStatus("0");
        order.setConsignStatus("0");
        order.setIsDelete("0");
        //todo 删除购物车数据,购物车数据落库之后,还需要对数据库中的数据进行逻辑删除;
        redisTemplate.delete("Cart_"+order.getUsername());
        return orderMapper.insertSelective(order);
    }
}
