package com.changgou.goods.feign;

import com.changgou.goods.pojo.Sku;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author pengmin
 * @date 2020/11/14 16:29
 */
@FeignClient(name = "goods",path = "/sku") // 根据服务名,通过eureka注册中心中远程进行调用;
public interface SkuFeign {

    /**
     * 从数据库中根据status状态查询sku;
     * @param status
     * @return
     */
    @GetMapping("/status/{status}")
    public Result<List<Sku>> findByStatus(@PathVariable(name = "status")String status);

    /**
     * 根据ID获取Sku信息;
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<Sku> findById(@PathVariable(value="id",required = true)Long id);

    /**
     * 根据条件搜索Sku集合;
     * @param sku
     * @return
     */
    @PostMapping(value = "/search" )
    public Result<List<Sku>> findList(@RequestBody(required = false) Sku sku);

    /**
     * 下单后减少商品库存数量;
     * @param id
     * @param num
     * @return
     */
    @GetMapping("/deCount")
    public Result deCount(@RequestParam(name = "id")Long id,@RequestParam(name = "num")Integer num);
}
