package com.changgou.order.controller;

import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ywh
 * @version 1.0.0
 * @date 2020/11/24 19:53
 */
@RestController
@RequestMapping("/testFeign")
public class TestFeign {

   /* @Autowired
    private SkuFeign skuFeign;

    @RequestMapping("/sku/{id}")
    public Result getSku(@PathVariable(name = "id")Long id){
        Result<Sku> res = skuFeign.findById(id);
        Sku sku = res.getData();
        System.out.println(sku);
        return new Result(true, StatusCode.OK,"ok",sku);
    }*/
}
