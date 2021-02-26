package com.changgou.goods.service.impl;

import com.changgou.core.service.impl.CoreServiceImpl;
import com.changgou.goods.dao.TemplateMapper;
import com.changgou.goods.pojo.Template;
import com.changgou.goods.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/****
 * @Author:admin
 * @Description:Template业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class TemplateServiceImpl extends CoreServiceImpl<Template> implements TemplateService {

    private TemplateMapper templateMapper;

    @Autowired
    public TemplateServiceImpl(TemplateMapper templateMapper) {
        super(templateMapper, Template.class);
        this.templateMapper = templateMapper;
    }

    /**
     * 根据分类ID获取模板信息;
     * @param id
     * @return
     */
    @Override
    public Template findByCategoryId(Integer id) {
        Template template = templateMapper.selectByPrimaryKey(id);
        return template;
    }
}
