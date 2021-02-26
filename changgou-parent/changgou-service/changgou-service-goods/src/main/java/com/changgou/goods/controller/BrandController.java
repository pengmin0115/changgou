package com.changgou.goods.controller;

import com.changgou.core.AbstractCoreController;
import com.changgou.goods.pojo.Brand;
import com.changgou.goods.service.BrandService;
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
@RequestMapping("/brand")
@CrossOrigin
public class BrandController extends AbstractCoreController<Brand> {

    private BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService) {
        super(brandService, Brand.class);
        this.brandService = brandService;
    }

    /**
     * 根据分类ID查找对应品牌信息;
     * @param id
     * @return
     */
    @GetMapping("/category/{id}")
    public Result<List<Brand>> findBrandByCategory(@PathVariable(name = "id")Integer id){
        List<Brand> brandList = brandService.findBrandByCategory(id);
        return new Result<>(true, StatusCode.OK,"查询成功",brandList);
    }
}