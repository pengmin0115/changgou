package com.changgou.pay.confirm;

import com.alibaba.fastjson.JSON;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author pengmin
 * @date 2021/2/26 12:02
 */
@Component
public class PayConfirm implements RabbitTemplate.ConfirmCallback {

    public PayConfirm() {
    }

    @Value("${mq.pay.exchange.order}")
    private String exchange;

    @Value("${mq.pay.queue.order}")
    private String queue;

    @Value("${mq.pay.routing.key}")
    private String routing;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 消息投递confirm回调函数;
     * @param correlationData 消息
     * @param ack  是否成功
     * @param cause 失败原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            System.out.println("成功发送消息到交换机: " + cause);
        } else {
            System.out.println("message fail: " + cause);
            //  可以重试吗??
            /*System.out.println("获取到的消息是:"+correlationData.toString());
            System.out.println("开始重试..");
            rabbitTemplate.convertAndSend(exchange,routing, JSON.toJSONString(correlationData));*/
        }
    }
}
