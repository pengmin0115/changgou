package com.changgou.canal.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.changgou.goods.feign.SkuFeign;
import com.xpand.starter.canal.annotation.ListenPoint;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author pengmin
 * @date 2020/11/20 20:06
 */

public class SkuListener {

    @Autowired
    private SkuFeign skuFeign;



    /**
     * 监听sku表的修改,并动态生成商品的详情页;
     * @param eventType
     * @param rowData
     */
    @ListenPoint(destination = "example", schema = "changgou_goods", table = "tb_sku",
            eventType = {
                    CanalEntry.EventType.UPDATE,
                    CanalEntry.EventType.INSERT,
                    CanalEntry.EventType.DELETE})
    public void onEventCustomUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        // 获取ID
        String categoryId = getColumnValue(eventType, rowData);
        /*Result<List<Content>> result = contentFeign.findByCategory(Long.valueOf(categoryId));
        List<Content> contentList = result.getData();
        String jsonString = JSON.toJSONString(contentList);
        stringRedisTemplate.boundValueOps("content_"+categoryId).set(jsonString);*/
    }

    private String getColumnValue(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        // 判断是否是删除; 是删除只能获取事件发生之前的值;
        String sku_id = "";
        if (eventType == CanalEntry.EventType.DELETE) {
            List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
            for (CanalEntry.Column column : beforeColumnsList) {
                if ("sku_id".equals(column.getName())) {
                    sku_id = column.getValue();
                    break;
                }
            }
        } else {
            List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
            for (CanalEntry.Column column : afterColumnsList) {
                if ("sku_id".equals(column.getName())) {
                    sku_id = column.getValue();
                    break;
                }
            }
        }
        return sku_id;
    }
}
