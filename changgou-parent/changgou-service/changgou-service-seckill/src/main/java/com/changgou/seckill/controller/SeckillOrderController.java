package com.changgou.seckill.controller;

import com.changgou.core.AbstractCoreController;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.service.SeckillOrderService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/seckillOrder")
@CrossOrigin
public class SeckillOrderController extends AbstractCoreController<SeckillOrder> {

    private SeckillOrderService seckillOrderService;

    @Autowired
    public SeckillOrderController(SeckillOrderService seckillOrderService) {
        super(seckillOrderService, SeckillOrder.class);
        this.seckillOrderService = seckillOrderService;
    }

    /**
     * 下订单;
     * @return
     */
    @RequestMapping("/add")
    public Result addOrder(String time,Long id){
        //可从token中解析出用户名; tokenDecode.getName();
        String username = "user01";
        boolean flag =  seckillOrderService.add(time,id,username);
        if (flag){
            return new Result(true, StatusCode.OK,"ok");
        }
        return new Result(false, StatusCode.ERROR,"error");
    }
}
