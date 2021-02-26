package com.changgou.goods.service.impl;

import com.changgou.core.service.impl.CoreServiceImpl;
import com.changgou.goods.dao.SkuMapper;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.service.SkuService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/****
 * @Author:admin
 * @Description:Sku业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class SkuServiceImpl extends CoreServiceImpl<Sku> implements SkuService {

    private SkuMapper skuMapper;

    @Autowired
    public SkuServiceImpl(SkuMapper skuMapper) {
        super(skuMapper, Sku.class);
        this.skuMapper = skuMapper;
    }

    @Override
    public List<Sku> findByStatus(String status) {
        return skuMapper.findByStatus(status);
    }

    @Override
    public List<Sku> findList(Sku sku) {
        Example example = new Example(Sku.class);
        example.createCriteria().andEqualTo("spuId",sku.getSpuId());
        return skuMapper.selectByExample(example);
    }

    @Override
    public int deCount(Long id, Integer num) {
        return skuMapper.deCount(id,num);
    }

}
