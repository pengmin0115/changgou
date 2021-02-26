package com.changgou.content.dao;
import com.changgou.content.pojo.Content;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/****
 * @Author:admin
 * @Description:Content的Dao
 * @Date 2019/6/14 0:12
 *****/
public interface ContentMapper extends Mapper<Content> {
    /**
     * findByCategory;
     * @param id
     * @return
     */
    @Select("SELECT * from tb_content where category_id = #{id} and status = 1")
    List<Content> findByCategory(Long id);
}
