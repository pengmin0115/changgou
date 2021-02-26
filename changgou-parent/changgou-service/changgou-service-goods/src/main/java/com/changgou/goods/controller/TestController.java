package com.changgou.goods.controller;

import com.changgou.goods.dao.TestSkuMapper;
import com.changgou.goods.pojo.Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pengmin
 * @date 2020/12/15 22:33
 */
@RestController
@RequestMapping("/testSku")
public class TestController {
    @Autowired
    private TestSkuMapper testSkuMapper;

    @RequestMapping("/query")
    public Sku querySku(Integer id){
        return testSkuMapper.testSkuQuery(id);
    }

    @RequestMapping("/update")
    public void update(Integer price,Integer id){
        testSkuMapper.updateSkuPrice(price,id);
    }
}
