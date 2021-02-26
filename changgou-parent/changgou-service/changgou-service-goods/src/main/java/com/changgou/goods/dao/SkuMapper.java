package com.changgou.goods.dao;
import com.changgou.goods.pojo.Sku;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;
import java.util.List;
/****
 * @Author:admin
 * @Description:Sku的Dao
 * @Date 2019/6/14 0:12
 *****/
public interface SkuMapper extends Mapper<Sku> {
    /**
     * 根据Status值获取sku;
     * @param status
     * @return
     */
    @Select(value = "select * from tb_sku where status = #{status}")
    List<Sku> findByStatus(String status);

    /**
     * 减库存;
     * for update: 给数据库加上行锁,确保分布式事务线程之间的安全性;
     * @param id
     * @param num
     * @return
     */
    @Update(value = "update tb_sku set num = num-#{num} where id = #{id} and num >= #{num}")
    int deCount(Long id, Integer num);
}
