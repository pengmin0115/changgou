package com.changgou.goods.service;

import com.changgou.core.service.CoreService;
import com.changgou.goods.pojo.Category;

import java.util.List;

/****
 * @Author:admin
 * @Description:Category业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface CategoryService extends CoreService<Category> {
    /**
     * 根据父分类ID查找所有的直接子分类;
     * @param pid
     * @return
     */
    List<Category> findByParentId(Integer pid);
}
