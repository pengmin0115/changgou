package com.changgou.canal.listener;


import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.changgou.content.feign.ContentFeign;
import com.changgou.content.pojo.Content;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.ListenPoint;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * @author pengmin
 * @date 2020/11/13 20:43
 */

@CanalEventListener
public class CanalDataEventListener {

    @Autowired
    private ContentFeign contentFeign;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @ListenPoint(destination = "example", schema = "changgou_content", table = "tb_content",
            eventType = {
                    CanalEntry.EventType.UPDATE,
                    CanalEntry.EventType.INSERT,
                    CanalEntry.EventType.DELETE})
    public void onEventCustomUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        // 获取ID
        String categoryId = getColumnValue(eventType, rowData);
        Result<List<Content>> result = contentFeign.findByCategory(Long.valueOf(categoryId));
        List<Content> contentList = result.getData();
        String jsonString = JSON.toJSONString(contentList);
        stringRedisTemplate.boundValueOps("content_"+categoryId).set(jsonString);
    }

    /**
     * 获取对应发生改变的数据行的category_id字段对应的值;
     *
     * @param eventType 事件类型;CRUD
     * @param rowData   行数据;
     * @return
     */
    private String getColumnValue(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        // 判断是否是删除; 是删除只能获取事件发生之前的值;
        String category_id = "";
        if (eventType == CanalEntry.EventType.DELETE) {
            List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
            for (CanalEntry.Column column : beforeColumnsList) {
                if ("category_id".equals(column.getName())) {
                    category_id = column.getValue();
                    break;
                }
            }
        } else {
            List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
            for (CanalEntry.Column column : afterColumnsList) {
                if ("category_id".equals(column.getName())) {
                    category_id = column.getValue();
                    break;
                }
            }
        }
        return category_id;
    }

    // 同1事件只能被监听一次;
    /*@UpdateListenPoint
    public void onEvent1(CanalEntry.RowData rowData) {
        List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
        for (CanalEntry.Column column : beforeColumnsList) {
            System.out.println(column.getName() + ";" + column.getValue());
        }

        System.out.println("=======================");

        List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
        for (CanalEntry.Column column : afterColumnsList) {
            System.out.println(column.getName() + ";" + column.getValue());
        }
    }*/


}
