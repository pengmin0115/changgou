package com.changgou.goods.dao;
import com.changgou.goods.pojo.Brand;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/****
 * @Author:admin
 * @Description:Brand的Dao
 * @Date 2019/6/14 0:12
 *****/
public interface BrandMapper extends Mapper<Brand> {

    /**
     * 根据分类ID查找对应品牌信息;
     * @param id
     * @return
     */
    @Select("select * from tb_brand where id in " +
            "(select brand_id from tb_category_brand where category_id = #{id})")
    List<Brand> findBrandByCategory(Integer id);
}
