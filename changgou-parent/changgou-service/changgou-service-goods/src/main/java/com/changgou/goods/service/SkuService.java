package com.changgou.goods.service;

import com.changgou.core.service.CoreService;
import com.changgou.goods.pojo.Sku;

import java.util.List;

/****
 * @Author:admin
 * @Description:Sku业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface SkuService extends CoreService<Sku> {

    /**
     * 根据Status值获取sku;
     * @param status
     * @return
     */
    List<Sku> findByStatus(String status);


    /**
     * 据条件搜索Sku集合;
     * @param sku
     * @return
     */
    List<Sku> findList(Sku sku);

    /**
     * 下单后减少商品库存数量
     * @param id
     * @param num
     * @return
     */
    int deCount(Long id, Integer num);
}
