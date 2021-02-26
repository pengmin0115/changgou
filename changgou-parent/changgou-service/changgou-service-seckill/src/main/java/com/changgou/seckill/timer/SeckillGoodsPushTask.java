package com.changgou.seckill.timer;

import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.utils.DateUtil;
import entity.SystemConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author pengmin
 * @date 2020/11/28 17:01
 */
@Component
public class SeckillGoodsPushTask {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    /**
     * 使用spring的定时任务框架,将秒杀商品数据压入redis中;
     */
    // 定时任务注解;
    @Scheduled(cron = "0/5 * * * * ?")
    public void SeckillGoodsPushTask() {
        // 将商品注入redis;
        List<Date> dateMenus = DateUtil.getDateMenus();
        for (Date dateMenu : dateMenus) {
            Example example = new Example(SeckillGoods.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("startTime", dateMenu);
            criteria.andLessThan("endTime", DateUtil.addDateHour(dateMenu, 2));
            criteria.andGreaterThan("stockCount", 0);
            criteria.andEqualTo("status", "1");
            String time = DateUtil.data2str(dateMenu, "yyyyMMddHH");
            Set idSet = redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + time).keys();
            if (idSet != null && idSet.size() > 0) {
                // 避免重复添加数据;
                criteria.andNotIn("id", idSet);
            }
            List<SeckillGoods> seckillGoods = seckillGoodsMapper.selectByExample(example);
            for (SeckillGoods seckillGood : seckillGoods) {
                redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + time).put(seckillGood.getId().toString(), seckillGood);
                redisTemplate.boundHashOps(SystemConstants.SEC_KILL_GOODS_PREFIX + time).expire(2, TimeUnit.HOURS);
            }
        }
    }
}
