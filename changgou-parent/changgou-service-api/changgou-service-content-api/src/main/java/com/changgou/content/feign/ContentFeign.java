package com.changgou.content.feign;

import com.changgou.content.pojo.Content;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author pengmin
 * @date 2020/11/14 0:20
 */
@FeignClient(name = "content",path = "/content")  // 名字要和对应的为服务名字一致;
public interface ContentFeign {
    /**
     * 根据分类ID获取广告列表数据;
     * @param id
     * @return
     */
    @RequestMapping("/list/category/{id}")
    public Result<List<Content>> findByCategory(@PathVariable(name = "id")Long id);
}
