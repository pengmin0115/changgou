package com.changgou.order.listener;

import com.alibaba.fastjson.JSON;
import com.changgou.order.dao.OrderMapper;
import com.changgou.order.pojo.Order;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author pengmin
 * @date 2020/11/25 18:10
 */
@Component
@RabbitListener(queues = "queue.order")
public class PayOrderUpdateListener {

    @Autowired
    private OrderMapper orderMapper;

    @RabbitHandler
    public void handleMessage(String message){
        Map<String,String> map = JSON.parseObject(message, Map.class);
        if (map != null){
            if (map.get("return_code").equals("SUCCESS")){
                Order order = orderMapper.selectByPrimaryKey(map.get("out_trade_no"));
                if (map.get("result_code").equals("SUCCESS")){
                    // 支付成功; 更新属性&落库;
                    order.setUpdateTime(new Date());
                    String time_end = map.get("time_end");
                    try {
                        Date updateTime = new SimpleDateFormat("yyyyMMddHHmmss").parse(time_end);
                        order.setPayTime(updateTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //设置微信支付单号;
                    order.setTransactionId(map.get("transaction_id"));
                    order.setPayStatus("1");
                    orderMapper.updateByPrimaryKeySelective(order);
                }else{
                    // 支付失败;
                    order.setIsDelete("1");
                    orderMapper.updateByPrimaryKeySelective(order);
                }
            }else{
                // 异常;
                System.out.println("WeChat Server返回信息为空;");
            }
        }
    }
}
