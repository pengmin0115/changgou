package com.changgou.goods.controller;

import com.changgou.core.AbstractCoreController;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.service.SkuService;
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
@RequestMapping("/sku")
@CrossOrigin
public class SkuController extends AbstractCoreController<Sku> {

    private SkuService skuService;

    @Autowired
    public SkuController(SkuService skuService) {
        super(skuService, Sku.class);
        this.skuService = skuService;
    }

    /**
     * 从数据库中根据status状态查询sku;
     * @param status
     * @return
     */
    @GetMapping("/status/{status}")
    public Result<List<Sku>> findByStatus(@PathVariable(name = "status")String status){
        List<Sku> skuList = skuService.findByStatus(status);
        return new Result<>(true, StatusCode.OK,"ok",skuList);
    }

    /**
     * 根据条件搜索Sku集合;
     * @param sku
     * @return
     */
    @PostMapping(value = "/search" )
    public Result<List<Sku>> findList(@RequestBody(required = false) Sku sku){
        List<Sku> skuList = skuService.findList(sku);
        return new Result<>(true,StatusCode.OK,"ok",skuList);
    }

    /**
     * 根据ID获取Sku信息;
     * @param id
     * @return
     */
    /*@GetMapping("/{id}")
    Result<Sku> findById(@PathVariable(name="id")Long id){
        Sku sku = skuService.selectByPrimaryKey(id);
        return new Result<>(true,StatusCode.OK,"ok",sku);
    }*/

    /**
     * 下单后减少商品库存数量;
     * @param id
     * @param num
     * @return
     */
    @GetMapping("/deCount")
    public Result deCount(@RequestParam(name = "id")Long id,@RequestParam(name = "num")Integer num){
        int count = skuService.deCount(id,num);
        if (count > 0){
            return new Result(true,StatusCode.OK,"ok");
        }
        return new Result(true,StatusCode.ERROR,"fail");
    }
}
