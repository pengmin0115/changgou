package com.changgou.seckill.controller;

import com.changgou.core.AbstractCoreController;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.service.SeckillGoodsService;
import com.changgou.seckill.utils.DateUtil;
import entity.Result;
import entity.StatusCode;
import entity.SystemConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/seckillGoods")
@CrossOrigin
public class SeckillGoodsController extends AbstractCoreController<SeckillGoods> {

    private SeckillGoodsService seckillGoodsService;

    @Autowired
    public SeckillGoodsController(SeckillGoodsService seckillGoodsService) {
        super(seckillGoodsService, SeckillGoods.class);
        this.seckillGoodsService = seckillGoodsService;
    }

    /**
     * 获取时间菜单;
     * @return
     */
    @GetMapping("/menu")
    public List<Date> menu(){
        return DateUtil.getDateMenus();
    }

    /**
     * 根据不同时间段获取秒杀商品列表;
     * @param time
     * @return
     */
    @GetMapping("/list")
    public Result list(String time){
        List<SeckillGoods> seckillGoodsList = seckillGoodsService.list(time);
        return new Result(true, StatusCode.OK,"ok",seckillGoodsList);
    }

    @GetMapping("/detail")
    public Result getDetail(String time, Long id){
        SeckillGoods seckillGoods = seckillGoodsService.getDetail(time,id);
        return new Result(true,StatusCode.OK,"ok",seckillGoods);
    }

}
