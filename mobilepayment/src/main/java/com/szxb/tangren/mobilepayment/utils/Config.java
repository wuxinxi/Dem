package com.szxb.tangren.mobilepayment.utils;

/**
 * Created by Administrator on 2016/9/22 0022.
 */
public class Config {

    public static String payUrl = "https://pay.swiftpass.cn/pay/gateway";

    public static String downloadUrlString = "https://download.swiftpass.cn/gateway";

    public static String notify_url = "http://112.74.102.125:8088/native/testPayResult";

    public static String wechatService = "pay.weixin.native";

    public static String aliService = "pay.alipay.native";

    public static String tenService = "pay.qq.native";


    public static String mch_id = "105580000075";//大商户号

    public static String sign_agentno = "105570000016";//渠道号

    public static String mch_key = "c1b48bf94baed7757cb189301106e7a1";//商户秘钥

    public static String agento_key = "1acef66a8122cc8b0bac219db862af50";//渠道秘钥

    public static String merchant_id = "100510021146";//平台商户号（门店商户号）
}
