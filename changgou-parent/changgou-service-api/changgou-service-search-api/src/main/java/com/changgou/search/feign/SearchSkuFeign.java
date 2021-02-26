package com.changgou.search.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

/**
 * @author pengmin
 * @date 2020/11/17 17:03
 */
@FeignClient(name = "search",path = "/search")
public interface SearchSkuFeign {

    @PostMapping
    Map<String,Object> search(@RequestBody(required = false) Map<String, String> searchMap);
}
