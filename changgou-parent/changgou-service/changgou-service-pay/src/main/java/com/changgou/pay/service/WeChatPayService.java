package com.changgou.pay.service;

import java.util.Map;

/**
 * @author pengmin
 * @version 1.0.0
 * @date 2020/11/25 16:37
 */

public interface WeChatPayService {

    /**
     * 创建支付订单;
     * @param out_trade_no
     * @param total_fee
     * @return
     */
    Map<String, String> createNative(String out_trade_no, String total_fee);

    /**
     * 查询订单支付状态;
     * @param out_trade_no
     * @return
     */
    Map<String, String> queryStatus(String out_trade_no);
}
