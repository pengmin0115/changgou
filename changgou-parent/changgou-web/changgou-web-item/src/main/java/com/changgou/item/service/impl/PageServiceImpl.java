package com.changgou.item.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.CategoryFeign;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.item.service.PageService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pengmin
 * @date 2020/11/21 11:58
 */
@Service
public class PageServiceImpl implements PageService {

    @Autowired
    private CategoryFeign categoryFeign;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SpuFeign spuFeign;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${pagepath}")
    private String pagepath;

    @Override
    public void generateHtml(Long id) {
        Context context = new Context();
        Map<String, Object> dataMap = buildDataModel(id);
        context.setVariables(dataMap);
        File file = new File(pagepath);
        if (!file.exists()){
            file.mkdirs();
        }
        File destFile = new File(file, id + ".html");
        try {
            PrintWriter printWriter = new PrintWriter(destFile, "UTF-8");
            templateEngine.process("item",context,printWriter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 生成数据模型;
     * @param id
     * @return
     */
    public Map<String,Object> buildDataModel(Long id){
        Map<String,Object> map = new HashMap<>();
        //获取相关数据;
        Result<Spu> spuResult = spuFeign.findById(id);
        Spu spu = spuResult.getData();
        Sku sku = new Sku();
        sku.setSpuId(id);
        List<Sku> skuList = skuFeign.findList(sku).getData();
        //分类信息;
        map.put("category1",categoryFeign.findById(spu.getCategory1Id()).getData());
        map.put("category2",categoryFeign.findById(spu.getCategory2Id()).getData());
        map.put("category3",categoryFeign.findById(spu.getCategory3Id()).getData());
        if (spu.getImages()!=null){
            map.put("imageList",spu.getImages().split(","));
        }
        map.put("specList", JSON.parseObject(spu.getSpecItems(),Map.class));
        map.put("spu",spu);
        map.put("skuList",skuList);
        return map;
    }
}
