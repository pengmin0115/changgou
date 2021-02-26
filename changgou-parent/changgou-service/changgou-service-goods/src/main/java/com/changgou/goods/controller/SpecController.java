package com.changgou.goods.controller;

import com.changgou.core.AbstractCoreController;
import com.changgou.goods.pojo.Spec;
import com.changgou.goods.service.SpecService;
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
@RequestMapping("/spec")
@CrossOrigin
public class SpecController extends AbstractCoreController<Spec> {

    private SpecService specService;

    @Autowired
    public SpecController(SpecService specService) {
        super(specService, Spec.class);
        this.specService = specService;
    }

    /**
     * 通过分类ID查询参数列表信息;
     * @param id
     * @return
     */
    @GetMapping("/category/{id}")
    public Result<List<Spec>> findSpecByCategoryId(@PathVariable("id")Integer id){
       List<Spec> specList = specService.findSpecByCategoryId(id);
       return new Result<>(true, StatusCode.OK,"查询成功",specList);
    }
}
