package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.SkuElasticSearchMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SearchResultImpl;
import com.changgou.search.service.SkuService;
import entity.Result;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author pengmin
 * @date 2020/11/14 16:42
 */
@Service
public class SkuServiceImpl implements SkuService {
    @Autowired
    private SkuElasticSearchMapper skuElasticSearchMapper;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public void importSkuData() {
        Result<List<Sku>> listResult = skuFeign.findByStatus("1");
        List<Sku> skuList = listResult.getData();
        String jsonString = JSON.toJSONString(skuList);
        List<SkuInfo> skuInfos = JSON.parseArray(jsonString, SkuInfo.class);
        // 将spec字段中的字符串 转换为map 存储到specMap字段中;
        for (SkuInfo skuInfo : skuInfos) {
            String spec = skuInfo.getSpec();
            Map<String, Object> specMap = JSON.parseObject(spec, Map.class);
            skuInfo.setSpecMap(specMap);
        }
        skuElasticSearchMapper.saveAll(skuInfos);
    }

    @Override
    public Map<String, Object> search(Map<String, String> searchMap) {
        // 防止keyword为空, 为其设置默认值;
        String keywords = searchMap.get("keywords");
        if (StringUtils.isEmpty(keywords)) {
            keywords = "华为";
        }
        // 构建查询的条件对象的构建者;
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //设置商品参数数据查询的条件:分类,品牌,spec参数,etc;
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("categoryGroup").field("categoryName").size(10000));
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("brandGroup").field("brandName").size(10000));
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("specGroup").field("spec.keyword").size(10000));

        // 分页
        String pageNumString = searchMap.get("pageNum");
        if (StringUtils.isEmpty(pageNumString)) {
            pageNumString = "1";
        }
        Integer pageNum = Integer.valueOf(pageNumString);
        Integer pageSize = 40;
        //此处的分页组件,当前页数从0开始计;
        Pageable pageble = PageRequest.of(pageNum - 1, pageSize);
        nativeSearchQueryBuilder.withPageable(pageble);

        // 加入过滤条件;
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //品牌;分类;规格参数;价格; filter方法即表示必须满足的条件; MUST
        String brand = searchMap.get("brand");
        if (!StringUtils.isEmpty(brand)){
            boolQueryBuilder.filter(QueryBuilders.termQuery("brandName",brand));
        }

        String category = searchMap.get("category");
        if (!StringUtils.isEmpty(category)){
            boolQueryBuilder.filter(QueryBuilders.termQuery("categoryName",category));
        }

        for (Map.Entry<String, String> stringStringEntry : searchMap.entrySet()) {
            String key = stringStringEntry.getKey();
            //传过来的key参数形式:spec_keyword;
            if (key.startsWith("spec_")){
                String value = stringStringEntry.getValue();
                boolQueryBuilder.filter(QueryBuilders.termQuery("specMap."+key.substring(5)+".keyword",value));
            }
        }
        //价格参数格式: 0-500,3000-*;
        String price = searchMap.get("price");
        if (!StringUtils.isEmpty(price)){
            String[] split = price.split("-");
            if ("*".equals(split[1])){
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(split[0]));
            }else{
                boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").from(split[0],true).to(split[1],true));
            }
        }
        nativeSearchQueryBuilder.withFilter(boolQueryBuilder);

        //设置高亮;
        nativeSearchQueryBuilder.withHighlightBuilder(new HighlightBuilder()
                .preTags("<em style=\"color:red\">").postTags("</em>"));
        nativeSearchQueryBuilder.withHighlightFields(new HighlightBuilder.Field("name"));

        //设置查询条件; 此处使用匹配查询(matchQuery)--先分词再查询;
        nativeSearchQueryBuilder.withQuery(QueryBuilders.multiMatchQuery(keywords,"name","brandName","categoryName"));
        //构建查询的条件对象;
        NativeSearchQuery queryObject = nativeSearchQueryBuilder.build();

        // 设置高亮需要自定义实现类;
        AggregatedPage<SkuInfo> skuInfos = elasticsearchTemplate.queryForPage(queryObject, SkuInfo.class,new SearchResultImpl());
        // 当前页数据集合;
        List<SkuInfo> content = skuInfos.getContent();
        // 总数据条数;
        long totalElements = skuInfos.getTotalElements();
        // 总页数;
        int totalPages = skuInfos.getTotalPages();

        // 获取商品参数并返回;
        StringTerms specList = (StringTerms) skuInfos.getAggregation("specGroup");
        List<StringTerms.Bucket> specListBuckets = specList.getBuckets();
        Map<String, Set<String>> specListRes = new HashMap<>();
        if (specListBuckets != null) {
            for (StringTerms.Bucket specListBucket : specListBuckets) {
                String keyAsString = specListBucket.getKeyAsString();
                Map<String, String> map = JSON.parseObject(keyAsString, Map.class);
                for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
                    String key = stringStringEntry.getKey();
                    String value = stringStringEntry.getValue();
                    Set<String> valueSet = specListRes.get(key);
                    if (valueSet == null) {
                        valueSet = new HashSet<>();
                    }
                    valueSet.add(value);
                    specListRes.put(key, valueSet);
                }
            }
        }

        // 获取品牌参数列表;
        ArrayList<String> brandList = getResultList(skuInfos, "brandGroup");
        ArrayList<String> categoryList = getResultList(skuInfos, "categoryGroup");
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("content", content);
        resMap.put("totalElements", totalElements);
        resMap.put("totalPages", totalPages);
        resMap.put("brandList", brandList);
        resMap.put("categoryList", categoryList);
        resMap.put("specListRes", specListRes);
        resMap.put("pageNum", pageNum);
        resMap.put("pageSize", pageSize);
        return resMap;
    }

    /**
     * 获取查询结果集中的对应结果;
     *
     * @param skuInfos
     * @param groupName
     * @return
     */
    private ArrayList<String> getResultList(AggregatedPage<SkuInfo> skuInfos, String groupName) {
        ArrayList<String> list = new ArrayList<>();
        StringTerms brandList = (StringTerms) skuInfos.getAggregation(groupName);
        if (brandList != null) {
            for (StringTerms.Bucket bucket : brandList.getBuckets()) {
                list.add(bucket.getKeyAsString());
            }
        }
        return list;
    }
}
