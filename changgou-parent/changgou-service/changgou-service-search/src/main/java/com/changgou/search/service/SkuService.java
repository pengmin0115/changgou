package com.changgou.search.service;

import java.util.Map;

/**
 * @author pengmin
 * @date 2020/11/14 16:41
 */

public interface SkuService {

    void importSkuData();

    Map<String, Object> search(Map<String, String> map);
}
