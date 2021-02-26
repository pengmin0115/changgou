package com.changgou.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.core.service.impl.CoreServiceImpl;
import com.changgou.goods.dao.BrandMapper;
import com.changgou.goods.dao.CategoryMapper;
import com.changgou.goods.dao.SkuMapper;
import com.changgou.goods.dao.SpuMapper;
import com.changgou.goods.pojo.Goods;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.goods.service.SpuService;
import entity.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/****
 * @Author:admin
 * @Description:Spu业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class SpuServiceImpl extends CoreServiceImpl<Spu> implements SpuService {

    private SpuMapper spuMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    public SpuServiceImpl(SpuMapper spuMapper) {
        super(spuMapper, Spu.class);
        this.spuMapper = spuMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Goods goods) {
        Spu spu = goods.getSpu();
        Long spuId = spu.getId();
        List<Sku> skuList = goods.getSkuList();
        if (spuId != null) {
            // 更新spu
            spuMapper.updateByPrimaryKeySelective(spu);
            // 删除原来的sku
            Sku sku = new Sku();
            sku.setSpuId(spuId);
            skuMapper.delete(sku);
        } else {
            // 添加spu
            long id = idWorker.nextId();
            spu.setId(id);
            spuMapper.insert(spu);
        }
        // 再保存sku
        for (Sku sku : skuList) {
            long skuId = idWorker.nextId();
            sku.setId(skuId);
            // 设置name: sku_name: spu_name + spec
            StringBuffer skuName = new StringBuffer();
            skuName.append(spu.getName());
            Map<String, String> map = JSON.parseObject(sku.getSpec(), Map.class);
            for (Map.Entry<String, String> stringEntry : map.entrySet()) {
                String value = stringEntry.getValue();
                skuName.append(" ").append(value);
            }
            sku.setName(skuName.toString());
            // 设置sku属性;
            sku.setCreateTime(new Date());
            sku.setUpdateTime(sku.getCreateTime());
            sku.setSpuId(spu.getId());
            sku.setCategoryId(spu.getCategory3Id());
            sku.setCategoryName(categoryMapper.selectByPrimaryKey(spu.getCategory3Id()).getName());
            sku.setBrandName(brandMapper.selectByPrimaryKey(spu.getBrandId()).getName());
            skuMapper.insertSelective(sku);
        }
    }

    @Override
    public Goods findGoodsById(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> skuList = skuMapper.select(sku);
        Goods goods = new Goods();
        goods.setSpu(spu);
        goods.setSkuList(skuList);
        return goods;
    }

}
