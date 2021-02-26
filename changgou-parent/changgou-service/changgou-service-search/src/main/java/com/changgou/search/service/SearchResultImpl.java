package com.changgou.search.service;

import com.alibaba.fastjson.JSON;
import com.changgou.search.pojo.MyHighLightField;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregations;

import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author pengmin
 * @date 2020/11/16 21:11
 */

public class SearchResultImpl implements SearchResultMapper {
    @Override
    public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
        // 反射+注解获取高亮的字段及其set方法;
        List<String> fieldList = new ArrayList<>();
        List<Method> methodList = new ArrayList<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.getName().startsWith("set")) {
                methodList.add(declaredMethod);
            }
        }
        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(MyHighLightField.class)) {
                fieldList.add(declaredField.getName());
            }
        }
        List<T> results = new ArrayList<>();
        //设置高亮;
        SearchHits hits = response.getHits();
        if (hits == null || hits.getTotalHits() <= 0) {
            return new AggregatedPageImpl<>(results);
        }
        for (SearchHit hit : hits) {
            T skuInfo = JSON.parseObject(hit.getSourceAsString(), clazz);
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            for (String fieldName : fieldList) {
                if (highlightFields != null && highlightFields.get(fieldName) != null && highlightFields.get(fieldName).getFragments() != null) {
                    Text[] fragments = highlightFields.get(fieldName).fragments();
                    StringBuffer stringbuffer = new StringBuffer();
                    for (Text fragment : fragments) {
                        stringbuffer.append(fragment.string());
                    }
                    String highLightField = stringbuffer.toString();
                    for (Method method : methodList) {
                        if (method.getName().substring(3).equalsIgnoreCase(fieldName)) {
                            try {
                                method.invoke(skuInfo, highLightField);
                                break;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            results.add(skuInfo);
        }
        long total = hits.getTotalHits();
        Aggregations aggregations = response.getAggregations();
        String scrollId = response.getScrollId();
        return new AggregatedPageImpl<T>(results, pageable, total, aggregations, scrollId);
    }
}
