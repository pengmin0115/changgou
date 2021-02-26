package com.changgou.goods.service;

import com.changgou.core.service.CoreService;
import com.changgou.goods.pojo.Spec;

import java.util.List;

/****
 * @Author:admin
 * @Description:Spec业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface SpecService extends CoreService<Spec> {

    /**
     * 通过分类ID查询参数列表信息;
     * @param id
     * @return
     */
    List<Spec> findSpecByCategoryId(Integer id);
}
