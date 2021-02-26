package com.changgou.goods.dao;

import com.changgou.goods.pojo.Sku;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author pengmin
 * @date 2020/12/15 22:27
 */

public interface TestSkuMapper {
    /**
     * 查询;
     * @param id
     * @return
     */
    @Select("select * from tb_sku where id = #{id};")
    Sku testSkuQuery(Integer id);

    /**
     * 修改指定id的商品的价格;
     * @param price
     */
    @Update("update tb_check set price = #{price} where id = #{id};")
    void updateSkuPrice(@Param("price") Integer price,@Param("id")Integer id);
}
