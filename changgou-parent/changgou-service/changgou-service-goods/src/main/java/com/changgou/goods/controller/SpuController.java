package com.changgou.goods.controller;

import com.changgou.core.AbstractCoreController;
import com.changgou.goods.pojo.Goods;
import com.changgou.goods.pojo.Spu;
import com.changgou.goods.service.SpuService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/spu")
@CrossOrigin
public class SpuController extends AbstractCoreController<Spu> {

    private SpuService spuService;

    @Autowired
    public SpuController(SpuService spuService) {
        super(spuService, Spu.class);
        this.spuService = spuService;
    }

    /**
     * 修改或新增 商品;
     * @param goods
     * @return
     */
    @PostMapping("/save")
    public Result save(@RequestBody Goods goods) {
        spuService.save(goods);
        return new Result(true, StatusCode.OK, "ok");
    }

    /**
     * 根据商品SPU id获取spu,sku信息;
     * @param id
     * @return
     */
    @GetMapping("/goods/{id}")
    public Result<Goods> findGoodsById(@PathVariable("id") Long id) {
        Goods goods = spuService.findGoodsById(id);
        return new Result<>(true, StatusCode.OK, "ok", goods);
    }
}
