package com.szxb.tangren.mobilepayment.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.szxb.tangren.mobilepayment.application.CustiomApplication;
import com.szxb.tangren.mobilepayment.db.OrderDBManager;
import com.szxb.tangren.mobilepayment.httpclient.CallServer;
import com.szxb.tangren.mobilepayment.httpclient.HttpListener;
import com.szxb.tangren.mobilepayment.utils.Config;
import com.szxb.tangren.mobilepayment.utils.Parmas;
import com.szxb.tangren.mobilepayment.utils.Utils;
import com.szxb.tangren.mobilepayment.utils.singutils.XMlUtils;
import com.szxb.tangren.mobilepayment.view.view.PaymentView;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.CacheMode;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/9/22 0022.
 */
public class PaymentCompl implements PaymentPresenter {

    private static PaymentView view;

    private static Context context;

    private static String total_fee;

    private static String out_trade_no;

    private OrderDBManager manager;

    private int polling_times = 0;// 轮询次数

    public int isPay = 0;// 是否已经完成支付

    public boolean isshowDown = false;//是否结束循环

    private String err_msg = "";


    static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    SortedMap<Object, Object> map = new TreeMap<Object, Object>();
                    map.put("payType", msg.obj.toString());
                    map.put("cashier", Utils.fenToYuan(total_fee));
                    String xml = XMlUtils.changeMapToXml(map);
                    Log.d("XML", xml);

                    view.onResult(100, xml);
                    break;
                case 1:
                    view.onResult(400, msg.obj.toString());
                    break;
                case 2:
                    view.onResult(404, "Fail");
                    break;
                default:

                    break;
            }
        }
    };

    public PaymentCompl(PaymentView view) {
        this.view = view;
    }

    @Override
    public void doSweptPay(Context context, String amoutOfMoney, String out_trade_no,
                           String payCode, String body, String service) {
        this.context = context;
        this.out_trade_no = out_trade_no;
        manager = new OrderDBManager();
        Request<String> request = NoHttp.createStringRequest(Config.payUrl, RequestMethod.POST);
        request.setDefineRequestBodyForXML(Parmas.getswept_parmas(context, amoutOfMoney,
                out_trade_no, payCode, body, service));
        CallServer.getHttpclient().add(context, "", 0, request, dosweptPay, false, true);
    }

    @Override
    public void doSweptCheckOrder(Context context, String out_trade_no) {
        Request<String> request = NoHttp.createStringRequest(Config.payUrl, RequestMethod.POST);
        request.setDefineRequestBodyForXML(Parmas.getQuery(context, out_trade_no));
        CallServer.getHttpclient().add(context, "", 4, request, queryOrderCallback, false, true);

    }

    HttpListener<String> dosweptPay = new HttpListener<String>() {
        @Override
        public void success(int what, Response<String> response) {
            Logger.d(response.get());
            Message message = Message.obtain();
            Map<String, String> xml = XMlUtils.decodeXml(response.get());
            String status = xml.get("status");
            // 当满足下面的条件时说明已经支付成功
            if (Integer.valueOf(status) == 0) {

                String resultCode = xml.get("result_code");
                if (Integer.valueOf(resultCode) == 0) {
                    total_fee = xml.get("total_fee");
                    out_trade_no = xml.get("out_trade_no");
                    String transaction_id = xml.get("transaction_id");
                    String time_end = xml.get("time_end");
                    String trade_type = xml.get("trade_type");
                    err_msg = xml.get("err_msg");
                    Logger.d("err_msg:" + err_msg);

                    String payType = "";

                    if (trade_type.equals("pay.weixin.native") || trade_type.equals("pay.weixin.micropay"))
                        payType = "微信支付";
                    else if (trade_type.equals("pay.alipay.micropay") || trade_type.equals("pay.alipay.native"))
                        payType = "支付宝支付";
                    else if (trade_type.equals("pay.qq.native") || trade_type.equals("pay.qq.micropay"))
                        payType = "QQ支付";

                    SortedMap<Object, Object> map = new TreeMap<Object, Object>();
                    map.put("payType", payType);
                    map.put("out_trade_no", out_trade_no);
                    map.put("time_end", time_end);
                    map.put("transaction_id", transaction_id);
                    map.put("cashier", Utils.fenToYuan(total_fee));
                    String result = XMlUtils.changeMapToXml(map);


                    view.onResult(100, result);

                } else if (Integer.valueOf(resultCode) == 1) {
                    String err_code = xml.get("err_code");

                    if (err_code.equals("USERPAYING")) {
                        Toast.makeText(context, "请在1分钟内输入密码,完成支付！",
                                Toast.LENGTH_LONG).show();
                        Logger.d(response.get());
                        // 等待用户输入完密码，进行查询，每6S查询1次,10次之后还没有完成支付则以冲正处理

                        ExecutorService executorService = Executors
                                .newSingleThreadExecutor();
                        executorService.execute(new Runnable() {

                            @Override
                            public void run() {

                                while (isPay == 0 && polling_times <20) {

                                    try {

                                        Thread.sleep(6 * 10 * 100);

                                        polling_times++;

                                        //查询订单
                                        requestQuery(context, out_trade_no);

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });

                        executorService.shutdown();

                    } else if (err_code.equals("AUTH_CODE_INVALID")) {

                        view.onResult(400, response.get());
                    }


                } else {

                    view.onResult(400, response.get());
                }
            } else {
                view.onResult(400, "请检查配置参数！");
            }
        }

        @Override
        public void fail(int what, Response<String> response) {

            view.onResult(400, "网络异常,请检查网络！");
        }
    };

    private int j = 0;

    //轮训订单(同步轮训)
    private void requestQuery(Context context, String out_trade_no) {
        Request<String> request = NoHttp.createStringRequest(Config.payUrl, RequestMethod.POST);
        request.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);
        request.setDefineRequestBodyForXML(Parmas.getQuery(context, out_trade_no));
        Response<String> response = NoHttp.startRequestSync(request);
        if (response.isSucceed()) {

            j++;
            System.out.println("j=" + j);
            CustiomApplication customApplication = (CustiomApplication) context.getApplicationContext();
            Map<String, String> xml = XMlUtils.decodeXml(response.get());
            int status = Integer.valueOf(xml.get("status"));
            if (status == 0) {
                int result_code = Integer.valueOf(xml.get("result_code"));
                if (result_code == 0) {
                    if (xml.get("trade_state").equals("SUCCESS")) {
                        isPay = 1;

                        total_fee = xml.get("total_fee");
                        out_trade_no = xml.get("out_trade_no");
                        String transaction_id = xml.get("transaction_id");
                        String time_end = xml.get("time_end");
                        String trade_type = xml.get("trade_type");//支付类型

                        String payType = "";

//                        try {
//
//                            if (trade_type.equals("pay.weixin.native") || trade_type.equals("pay.weixin.micropay"))
//                                manager.addRecord(out_trade_no, time_end, transaction_id, Utils.fenToYuan(total_fee), "微信支付", Utils.getDate());
//                            else if (trade_type.equals("pay.alipay.native") || trade_type.equals("pay.alipay.micropay"))
//                                manager.addRecord(out_trade_no, time_end, transaction_id, Utils.fenToYuan(total_fee), "支付宝支付", Utils.getDate());
//                            else if (trade_type.equals("pay.qq.native") || trade_type.equals("pay.qq.micropay"))
//                                manager.addRecord(out_trade_no, time_end, transaction_id, Utils.fenToYuan(total_fee), "QQ支付", Utils.getDate());
//
//                        } catch (Exception e) {
//                            Logger.d("交易记录存储失败，失败详情：" + e.toString());
//                        }


                        if (trade_type.equals("pay.weixin.native") || trade_type.equals("pay.weixin.micropay"))
                            payType = "微信支付";
                        else if (trade_type.equals("pay.alipay.micropay") || trade_type.equals("pay.alipay.native"))
                            payType = "支付宝支付";

                        SortedMap<Object, Object> map = new TreeMap<Object, Object>();
                        map.put("payType", payType);
                        map.put("out_trade_no", out_trade_no);
                        map.put("time_end", time_end);
                        map.put("transaction_id", transaction_id);
                        map.put("cashier", Utils.fenToYuan(total_fee));
                        String result = XMlUtils.changeMapToXml(map);


                        view.onResult(100, result);

                    } else if (xml.get("trade_state").equals("USERPAYING")) {
                        isPay = 0;
                    } else if (xml.get("trade_state").equals("REVOKED")) {
                        isPay = 0;
                    }
                }
            }

            Logger.d(response.get());
        } else {
            view.onResult(400, "网络异常,请检查网络！");
//            handler.sendEmptyMessage(2);
        }
    }


    HttpListener<String> queryOrderCallback = new HttpListener<String>() {
        @Override
        public void success(int what, Response<String> response) {
            Logger.d(response.get());
            Map<String, String> xml = XMlUtils.decodeXml(response.get());
            int status = Integer.valueOf(xml.get("status"));
            if (status == 0) {
                int result_code = Integer.valueOf(xml.get("result_code"));
                if (result_code == 0) {
                    String trade_state = xml.get("trade_state");
                    Logger.d("trade_trade:" + trade_state);
                    if (trade_state.equals("SUCCESS")) {

                        total_fee = xml.get("total_fee");
                        out_trade_no = xml.get("out_trade_no");
                        String transaction_id = xml.get("transaction_id");
                        String time_end = xml.get("time_end");
                        String trade_type = xml.get("trade_type");//支付类型

                        String payType = "";

                        if (trade_type.equals("pay.weixin.native") || trade_type.equals("pay.weixin.micropay"))
                            payType = "微信支付";
                        else if (trade_type.equals("pay.alipay.micropay") || trade_type.equals("pay.alipay.native"))
                            payType = "支付宝支付";
                        else if (trade_type.equals("pay.qq.native") || trade_type.equals("pay.qq.micropay")) {
                            payType = "QQ支付";
                        }

                        SortedMap<Object, Object> map = new TreeMap<Object, Object>();
                        map.put("payType", payType);
                        map.put("out_trade_no", out_trade_no);
                        map.put("time_end", time_end);
                        map.put("transaction_id", transaction_id);
                        map.put("cashier", Utils.fenToYuan(total_fee));
                        String result = XMlUtils.changeMapToXml(map);

                        view.onResult(100, result);

                    } else if (trade_state.equals("REFUND")) {
                        view.onResult(400, "已转入退款！");
                    } else if (trade_state.equals("NOTPAY")) {
                        view.onResult(200, "暂未完成支付！");
                    } else if (trade_state.equals("CLOSE")) {
                        view.onResult(400, "已关闭订单！");
                    } else if (trade_state.equals("PAYERROR")) {
                        view.onResult(400, "支付失败！");
                    }
                }
            } else {
                view.onResult(300, response.get());
            }
        }

        @Override
        public void fail(int what, Response<String> response) {
            view.onResult(400, "网络异常,请检查网络！");
        }
    };

}
