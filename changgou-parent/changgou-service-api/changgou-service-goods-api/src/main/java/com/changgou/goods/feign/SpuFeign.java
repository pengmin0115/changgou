package com.changgou.goods.feign;

import com.changgou.goods.pojo.Spu;
import entity.Result;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author pengmin
 * @date 2020/11/21 11:50
 */
@FeignClient(name = "goods",path = "/spu")
public interface SpuFeign {

    /***
     * 根据SpuID查询Spu信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Spu> findById(@PathVariable(name = "id") Long id);
}
