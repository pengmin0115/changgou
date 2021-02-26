package com.changgou.seckill.service.impl;

import com.changgou.core.service.impl.CoreServiceImpl;
import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.dao.SeckillOrderMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.service.SeckillOrderService;
import entity.IdWorker;
import entity.SystemConstants;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/****
 * @Author:admin
 * @Description:SeckillOrder业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class SeckillOrderServiceImpl extends CoreServiceImpl<SeckillOrder> implements SeckillOrderService {

    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private Redisson redisson;

    @Autowired
    public SeckillOrderServiceImpl(SeckillOrderMapper seckillOrderMapper) {
        super(seckillOrderMapper, SeckillOrder.class);
        this.seckillOrderMapper = seckillOrderMapper;
    }

    @Override
    public boolean add(String time, Long id, String username) {
        username = UUID.randomUUID().toString();

        // 获取锁;(RedLock--redisson实现的)
        RLock lock = redisson.getLock("myLock"+id);
        SeckillGoods seckillGoods = null;
        try {
            // 上锁;
            lock.lock(10, TimeUnit.SECONDS);
            seckillGoods = (SeckillGoods) redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + time).get(id.toString());
            if (seckillGoods == null || seckillGoods.getStockCount() <= 0) {
                throw new RuntimeException("sold out...");
            }
            // 下单,设置秒杀订单信息;
            SeckillOrder seckillOrder = new SeckillOrder();
            seckillOrder.setId(idWorker.nextId());
            seckillOrder.setSeckillId(id);
            //costPrice为秒杀价格;
            seckillOrder.setMoney(seckillGoods.getCostPrice());
            seckillOrder.setUserId(username);
            seckillOrder.setCreateTime(new Date());
            seckillOrder.setStatus("0");
            // 将秒杀订单存入redis中;
            redisTemplate.boundHashOps(SystemConstants.SEC_KILL_ORDER_KEY+time).put(username,seckillOrder);
            // 减库存,更新mysql;
            seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
            redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX+time).put(id.toString(),seckillGoods);
        } finally {
            // 释放锁;
            lock.unlock();
        }
        if (seckillGoods.getStockCount()<=0){
            // 只有当商品的库存数量降为0才去更新mysql数据库中的信息;
            // 减少每次秒杀下单都去操作数据库的操作;
            seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);
            //删除redis中对应的商品数据;
            redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX+time).delete(id.toString());
        }
        return true;
    }
}
