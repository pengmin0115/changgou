package com.changgou.pay.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.pay.confirm.PayConfirm;
import com.changgou.pay.confirm.PayReturnCallback;
import com.changgou.pay.service.WeChatPayService;
import com.github.wxpay.sdk.WXPayUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * @author pengmin
 * @version 1.0.0
 * @date 2020/11/25 16:27
 */
@RestController
@RequestMapping("/weChat/pay")
public class WeChatPayController {

    private static final String WX_SUCCESS = "<xml>\n" +
            "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
            "  <return_msg><![CDATA[OK]]></return_msg>\n" +
            "</xml>";
    private static final String WX_FAIL = "<xml>\n" +
            "  <return_code><![CDATA[FAIL]]></return_code>\n" +
            "  <return_msg><![CDATA[error]]></return_msg>\n" +
            "</xml>";

    @Autowired
    private WeChatPayService weChatPayService;

    @Value("${mq.pay.exchange.order}")
    private String exchange;

    @Value("${mq.pay.queue.order}")
    private String queue;

    @Value("${mq.pay.routing.key}")
    private String routing;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private PayConfirm payConfirm;

    @Autowired
    private PayReturnCallback payReturnCallback;

    /**
     * 调用下单API生成二维码code_url;
     *
     * @param out_trade_no
     * @param total_fee
     * @return
     */
    @RequestMapping("/create/native")
    public Result createNative(String out_trade_no, String total_fee) {
        Map<String, String> resMap = weChatPayService.createNative(out_trade_no, total_fee);
        return new Result(true, StatusCode.OK, "创建二维码成功", resMap);
    }

    /**
     * 查询订单支付状态;
     *
     * @param out_trade_no
     * @return
     */
    @GetMapping("/status/query")
    public Result queryStatus(String out_trade_no) {
        Map<String, String> resMap = weChatPayService.queryStatus(out_trade_no);
        return new Result(true, StatusCode.OK, "ok", resMap);
    }

    /**
     * 接收支付状态的通知;
     *
     * @param request
     * @return
     */
    @RequestMapping("/notify")
    public String notifyNative(HttpServletRequest request) {
        InputStream inputStream;
        try {
            inputStream = request.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, length);
            }
            out.close();
            inputStream.close();
            String str = new String(out.toByteArray(), "utf-8");
            System.out.println(str);
            Map<String, String> map = WXPayUtil.xmlToMap(str);

            // 将支付成功的通知消息,发送到MQ中;
            // 添加发送方消息确认机制;
            // todo 工作中可以多看看别人的回调是如何处理的;
            rabbitTemplate.setConfirmCallback(payConfirm);
            rabbitTemplate.setReturnCallback(payReturnCallback);
            rabbitTemplate.convertAndSend(exchange, routing, JSON.toJSONString(map));
            return WX_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return WX_FAIL;
    }


}
