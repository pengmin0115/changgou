package com.changgou.search.dao;

import com.changgou.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author pengmin
 * @date 2020/11/14 16:33
 */

public interface SkuElasticSearchMapper extends ElasticsearchRepository<SkuInfo,Long> {

}
