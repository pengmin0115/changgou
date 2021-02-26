package com.changgou.goods.service.impl;

import com.changgou.core.service.impl.CoreServiceImpl;
import com.changgou.goods.dao.CategoryMapper;
import com.changgou.goods.dao.SpecMapper;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Spec;
import com.changgou.goods.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/****
 * @Author:admin
 * @Description:Spec业务层接口实现类
 * @Date 2019/6/14 0:16
 *****/
@Service
public class SpecServiceImpl extends CoreServiceImpl<Spec> implements SpecService {

    private SpecMapper specMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    public SpecServiceImpl(SpecMapper specMapper) {
        super(specMapper, Spec.class);
        this.specMapper = specMapper;
    }

    /**
     * 通过分类ID查询参数列表信息;
     * @param id
     * @return
     */
    @Override
    public List<Spec> findSpecByCategoryId(Integer id) {
        Category category = categoryMapper.selectByPrimaryKey(id);
        Spec condition = new Spec();
        condition.setTemplateId(category.getTemplateId());
        List<Spec> specList = specMapper.select(condition);
        return specList;
    }
}
