package com.changgou.pay.service.impl;

import com.changgou.pay.service.WeChatPayService;
import com.github.wxpay.sdk.WXPayUtil;
import entity.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pengmin
 * @version 1.0.0
 * @date 2020/11/25 16:37
 */

@Service
public class WeChatPayServiceImpl implements WeChatPayService {

    @Value("${weixin.appid}")
    private String appId;

    @Value("${weixin.partner}")
    private String partner;

    @Value("${weixin.partnerkey}")
    private String partnerKey;

    @Value("${weixin.notifyurl}")
    private String notifyUrl;

    @Override
    public Map<String, String> createNative(String out_trade_no, String total_fee) {
        try {
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("appid",appId);
            paramsMap.put("mch_id",partner);
            //设置随机字符串
            paramsMap.put("nonce_str", WXPayUtil.generateNonceStr());
            paramsMap.put("body","changGou");
            paramsMap.put("spbill_create_ip","127.0.0.1");
            paramsMap.put("notify_url",notifyUrl);
            paramsMap.put("trade_type","NATIVE");
            paramsMap.put("out_trade_no",out_trade_no);
            paramsMap.put("total_fee",total_fee);
            String signedXml = WXPayUtil.generateSignedXml(paramsMap, partnerKey);
            // 发送https请求
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            httpClient.setHttps(true);
            httpClient.setXmlParam(signedXml);
            httpClient.post();
            // 获取请求的响应；
            String content = httpClient.getContent();
            Map<String, String> resMap = WXPayUtil.xmlToMap(content);
            //将客户端需要的参数进行封装;
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("code_url",resMap.get("code_url"));
            dataMap.put("out_trade_no",out_trade_no);
            dataMap.put("total_fee",total_fee);
            return dataMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, String> queryStatus(String out_trade_no) {
        try {
            Map<String, String> paramMap = new HashMap<>();
            paramMap.put("appid",appId);
            paramMap.put("mch_id",partner);
            //设置随机字符串;
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
            paramMap.put("out_trade_no",out_trade_no);
            String signedXml = WXPayUtil.generateSignedXml(paramMap, partnerKey);
            // 发送https请求;
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setHttps(true);
            httpClient.setXmlParam(signedXml);
            httpClient.post();
            // 获取请求结果返回;
            String content = httpClient.getContent();
            Map<String, String> map = WXPayUtil.xmlToMap(content);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
