package com.changgou.goods.controller;

import com.changgou.core.AbstractCoreController;
import com.changgou.goods.pojo.Para;
import com.changgou.goods.service.ParaService;
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
@RequestMapping("/para")
@CrossOrigin
public class ParaController extends AbstractCoreController<Para> {

    private ParaService paraService;

    @Autowired
    public ParaController(ParaService paraService) {
        super(paraService, Para.class);
        this.paraService = paraService;
    }

    /**
     * 根据分类ID获取参数信息;
     * @param id
     * @return
     */
    @GetMapping("/category/{id}")
    public Result<List<Para>> findParaByCategoryId(@PathVariable("id")Integer id){
       List<Para> paraList = paraService.findParaByCategoryId(id);
       return new Result<>(true, StatusCode.OK,"获取参数成功",paraList);
    }
}
