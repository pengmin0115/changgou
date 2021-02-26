package com.changgou.goods.service;

import com.changgou.core.service.CoreService;
import com.changgou.goods.pojo.Template;

import java.util.List;

/****
 * @Author:admin
 * @Description:Template业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface TemplateService extends CoreService<Template> {

    /**
     * 根据分类ID获取模板信息;
     * @param id
     * @return
     */
    Template findByCategoryId(Integer id);
}
