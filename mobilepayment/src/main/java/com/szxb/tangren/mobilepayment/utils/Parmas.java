package com.szxb.tangren.mobilepayment.utils;


import android.content.Context;

import com.szxb.tangren.mobilepayment.utils.singutils.MD5;
import com.szxb.tangren.mobilepayment.utils.singutils.SignUtils;
import com.szxb.tangren.mobilepayment.utils.singutils.XMlUtils;
import com.yolanda.nohttp.Logger;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class Parmas {

    /**
     * 普通模式：大商户号+商户秘钥
     * <p/>
     * 渠道模式：渠道号+渠道秘钥+门店商户号
     *
     * @author TangRen
     * @param args
     * @time 2016-7-8
     */
    private static String out_trade_no;// 商户订单号
    private static Sputils sputils;


    //被扫
    public static String getswept_parmas(Context context, String amount, String out_trade_no, String pay_code, String body, String service) {
        sputils = new Sputils();
        int tradModel = (int) sputils.get(context, "tradModel", 1);
        String key;
        String xmlString;
        SortedMap<Object, Object> map = new TreeMap<Object, Object>();
        map.put("auth_code", pay_code);
        map.put("body", body);
        map.put("charset", "UTF-8");
        map.put("mch_create_ip", Utils.localIp());
        map.put("nonce_str", Utils.Random(15));
        map.put("total_fee", Utils.yuanToFen(amount));
        map.put("service", service);
        map.put("out_trade_no", out_trade_no);

        if (tradModel == 1) {
            map.put("sign_agentno", sputils.get(context, "signAgentno", "").toString());
            map.put("mch_id", sputils.get(context, "merchantId", "").toString());
            key = sputils.get(context, "agentnoKey", "").toString();
        } else {
            map.put("mch_id", sputils.get(context, "mchId", "").toString());
            key = sputils.get(context, "mchKey", "").toString();
        }

        String sign = SignUtils.toSign("UTF-8", map, key);

        map.put("sign", sign);
        Logger.d("sign签名：" + sign);

        xmlString = XMlUtils.changeMapToXml(map);
        Logger.d(xmlString);
        return xmlString;
    }

    //主扫:订单
    public static String getScanPay(Context context, String amoutOfMoney, String out_trade_no, String body, String service) {
        sputils = new Sputils();
        int tradModel = (int) sputils.get(context, "tradModel", 1);
        String key;
        String xmlString;
        SortedMap<Object, Object> map = new TreeMap<Object, Object>();
        map.put("charset", "UTF-8");
        map.put("mch_create_ip", Utils.localIp());
        map.put("body", body);
        map.put("nonce_str", Utils.Random(15));

        map.put("notify_url", Config.notify_url);

        map.put("total_fee", Utils.yuanToFen(amoutOfMoney));
        map.put("service", service);
        map.put("out_trade_no", out_trade_no);
        if (tradModel == 1) {
            map.put("sign_agentno", sputils.get(context, "signAgentno", "").toString());
            map.put("mch_id", sputils.get(context, "merchantId", "").toString());
            key = sputils.get(context, "agentnoKey", "").toString();
        } else {
            map.put("mch_id", sputils.get(context, "mchId", "").toString());
            key = sputils.get(context, "mchKey", "").toString();
        }

        String sign = SignUtils.toSign("UTF-8", map, key);

        map.put("sign", sign);
        Logger.d("sign签名：" + sign);

        xmlString = XMlUtils.changeMapToXml(map);
        Logger.d(xmlString);
        return xmlString;
    }

    //主扫订单轮训
    public static String getQuery(Context context, String trade_no) {
        sputils = new Sputils();
        int tradModel = (int) sputils.get(context, "tradModel", 1);
        String key;
        StringBuffer xml = new StringBuffer();
        String xmlString = "";
        SortedMap<Object, Object> map = new TreeMap<Object, Object>();
        map.put("charset", "UTF-8");
        map.put("service", "unified.trade.query");
        map.put("nonce_str", Utils.Random(15));
        map.put("out_trade_no", trade_no);
        if (tradModel == 1) {
            map.put("sign_agentno", sputils.get(context, "signAgentno", "").toString());
            map.put("mch_id", sputils.get(context, "merchantId", "").toString());
            key = sputils.get(context, "agentnoKey", "").toString();
        } else {
            map.put("mch_id", sputils.get(context, "mchId", "").toString());
            key = sputils.get(context, "mchKey", "").toString();
        }

        String sign = SignUtils.toSign("UTF-8", map, key);

        map.put("sign", sign);
        Logger.d("sign签名：" + sign);

        xmlString = XMlUtils.changeMapToXml(map);
        Logger.d(xmlString);
        return xmlString;
    }

    /**
     * 退款
     *
     * @param out_trade_no
     * @return
     */
    public static String reFundArgs(Context context, String out_trade_no, String fee) {
        sputils = new Sputils();
        int tradModel = (int) sputils.get(context, "tradModel", 1);
        String key;
        String xmlString = "";
        SortedMap<Object, Object> map = new TreeMap<Object, Object>();
        map.put("charset", "UTF-8");
        map.put("service", "unified.trade.refund");
        map.put("nonce_str", Utils.Random(15));
        map.put("out_trade_no", out_trade_no);
        map.put("out_refund_no", Utils.OrderNo());
        map.put("refund_fee", Utils.yuanToFen(fee));
        map.put("total_fee", Utils.yuanToFen(fee));
        map.put("op_user_id", Config.mch_id);

        if (tradModel == 1) {
            map.put("sign_agentno", sputils.get(context, "signAgentno", "").toString());
            map.put("mch_id", sputils.get(context, "merchantId", "").toString());
            key = sputils.get(context, "agentnoKey", "").toString();
        } else {
            map.put("mch_id", sputils.get(context, "mchId", "").toString());
            key = sputils.get(context, "mchKey", "").toString();
        }
        String sign = SignUtils.toSign("UTF-8", map, key);

        map.put("sign", sign);
        Logger.d("sign签名：" + sign);

        // 转换成xml格式
        xmlString = XMlUtils.changeMapToXml(map);

        System.out.println("sign签名:" + sign);
        System.out.println("xml:" + xmlString);

        return xmlString;
    }



    public static String createSign(String characterEncoding,
                                    SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k)
                    && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + Config.mch_key);
        String sign = MD5.MD5Encode(sb.toString(), characterEncoding)
                .toUpperCase();
        System.out.println("签名：" + sign);
        return sign;
    }

    public static String downloadBill() {
        String xmlString = null;
        SortedMap<Object, Object> map = new TreeMap<Object, Object>();
        map.put("service", "pay.bill.agent");
        map.put("bill_date", "20160820");
        map.put("bill_type", "ALL");
        map.put("mch_id", Config.sign_agentno);
        map.put("nonce_str", Utils.Random(15));

        String sing = createSign("UTF-8", map);
        map.put("sign", sing);
        xmlString = XMlUtils.changeMapToXml(map);
        System.out.println(xmlString);
        return xmlString;
    }

    //冲正
    public static String Impact(Context context, String out_trade_no) {
        sputils = new Sputils();
        int tradModel = (int) sputils.get(context, "tradModel", 1);
        String key;
        String xmlString = "";
        SortedMap<Object, Object> map = new TreeMap<Object, Object>();
        map.put("charset", "UTF-8");
        map.put("service", "unified.micropay.reverse");
        map.put("nonce_str", Utils.Random(15));
        map.put("out_trade_no", out_trade_no);

        if (tradModel == 1) {
            map.put("sign_agentno", sputils.get(context, "signAgentno", "").toString());
            map.put("mch_id", sputils.get(context, "merchantId", "").toString());
            key = sputils.get(context, "agentnoKey", "").toString();
        } else {
            map.put("mch_id", sputils.get(context, "mchId", "").toString());
            key = sputils.get(context, "mchKey", "").toString();
        }
        String sign = SignUtils.toSign("UTF-8", map, key);

        map.put("sign", sign);
        Logger.d("sign签名：" + sign);

        // 转换成xml格式
        xmlString = XMlUtils.changeMapToXml(map);

        System.out.println("sign签名:" + sign);
        System.out.println("xml:" + xmlString);

        return xmlString;
    }
}
