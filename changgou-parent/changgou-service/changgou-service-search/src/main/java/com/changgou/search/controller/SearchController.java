package com.changgou.search.controller;

import com.changgou.search.service.SkuService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pengmin
 * @date 2020/11/14 16:37
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SkuService skuService;

    @GetMapping("/import")
    public Result importSkuData(){
        skuService.importSkuData();
        return new Result(true, StatusCode.OK,"ok");
    }

    @PostMapping
    public Map<String,Object> search(@RequestBody(required = false) Map<String, String> searchMap){
        if (searchMap == null){
            searchMap = new HashMap<>();
        }
        return skuService.search(searchMap);
    }
}
