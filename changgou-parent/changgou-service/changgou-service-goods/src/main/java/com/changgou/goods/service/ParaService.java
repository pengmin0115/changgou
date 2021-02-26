package com.changgou.goods.service;

import com.changgou.core.service.CoreService;
import com.changgou.goods.pojo.Para;

import java.util.List;

/****
 * @Author:admin
 * @Description:Para业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface ParaService extends CoreService<Para> {

    /**
     * 根据分类ID获取参数信息;
     * @param id
     * @return
     */
    List<Para> findParaByCategoryId(Integer id);
}
