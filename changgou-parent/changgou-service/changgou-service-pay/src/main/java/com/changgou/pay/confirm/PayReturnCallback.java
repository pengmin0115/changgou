package com.changgou.pay.confirm;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author pengmin
 * @date 2021/2/26 12:17
 */

public class PayReturnCallback implements RabbitTemplate.ReturnCallback {
    /**
     *
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        // 一般记录log日志即可?? 因为这里回调只在消息发送到具体的消息队列中出现错误时才调用;
        // 即交换机绑定路由key出现问题;
    }
}
