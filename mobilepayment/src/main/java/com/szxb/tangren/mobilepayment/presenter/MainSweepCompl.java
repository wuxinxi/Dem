package com.szxb.tangren.mobilepayment.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.szxb.tangren.mobilepayment.application.CustiomApplication;
import com.szxb.tangren.mobilepayment.db.OrderDBManager;
import com.szxb.tangren.mobilepayment.httpclient.CallServer;
import com.szxb.tangren.mobilepayment.httpclient.HttpListener;
import com.szxb.tangren.mobilepayment.utils.Config;
import com.szxb.tangren.mobilepayment.utils.Parmas;
import com.szxb.tangren.mobilepayment.utils.Utils;
import com.szxb.tangren.mobilepayment.utils.singutils.XMlUtils;
import com.szxb.tangren.mobilepayment.view.view.MainSweepView;
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
 * Created by Administrator on 2016/9/24 0024.
 */
public class MainSweepCompl implements MainSweepPresenter {

    private static MainSweepView view;

    private Integer polling_times = 0;// 轮询次数

    public static int isPay = 0;// 是否已经完成支付

    public boolean isshowDown = false;//是否结束循环

    private String result;

    private static String out_trade_no;

    private static String total_fee;


    private OrderDBManager manager;

    private Context context;

    /**
     * 0:
     * 1:
     * 2:
     */
    static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    isPay = 1;
                    view.onResult(400, msg.obj.toString());
                    break;

            }
        }
    };

    public MainSweepCompl(MainSweepView view) {
        this.view = view;
    }


    @Override
    public void doMainSweepPay(Context context, String amoutOfMoney,
                               String out_trade_no, String body, String service) {
        this.out_trade_no = out_trade_no;
        this.context = context;
        Request<String> request = NoHttp.createStringRequest(Config.payUrl, RequestMethod.POST);
        request.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);
        request.setDefineRequestBodyForXML(Parmas.getScanPay(context, amoutOfMoney, out_trade_no, body, service));
        CallServer.getHttpclient().add(context, "", 1,
                request, mainSweptCallBack, false, true);
    }

    HttpListener<String> mainSweptCallBack = new HttpListener<String>() {
        @Override
        public void success(int what, Response<String> response) {

            Logger.d(response.get());
            Map<String, String> xml = XMlUtils.decodeXml(response.get());
            int status = Integer.valueOf(xml.get("status"));
            if (status == 400) {
                view.onResult(400, result);
            } else if (status == 0) {
                int result_code = Integer.valueOf(xml.get("result_code"));
                if (result_code == 0) {
                    String code_url = xml.get("code_url");
                    Logger.d("支付地址：" + code_url);
                    view.onUrlCode(code_url);

                    ExecutorService executorService = Executors
                            .newSingleThreadExecutor();
                    executorService.execute(new Runnable() {

                        @Override
                        public void run() {
                            CustiomApplication customApplication = (CustiomApplication) context.getApplicationContext();
                            while (resultPay()) {

                                try {

                                    Thread.sleep(6 * 10 * 100);

                                    polling_times++;

                                    requestQuery(context, out_trade_no);

                                    Logger.d("Thread count:" + Thread.activeCount() + "");

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

                    executorService.shutdown();

                } else {
                    view.onResult(400, result);
                }
            }
        }

        @Override
        public void fail(int what, Response<String> response) {
            view.onResult(400, "网络异常,请检查网络！");
        }
    };

    int j = 0;


    private boolean resultPay() {
        CustiomApplication customApplication = (CustiomApplication) context.getApplicationContext();
        if (isPay == 1) {
            return false;
        } else if (polling_times > 30) {
            return false;
        } else if (customApplication.getIsshowDown() == 1) {
            return false;
        }
        return true;
    }


    //轮训订单(同步轮训)
    private void requestQuery(Context context, String out_trade_no) {
        Request<String> request = NoHttp.createStringRequest(Config.payUrl, RequestMethod.POST);
        request.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);
        request.setDefineRequestBodyForXML(Parmas.getQuery(context, out_trade_no));
        Response<String> response = NoHttp.startRequestSync(request);
        Message message = Message.obtain();
        if (response.isSucceed()) {
            j++;
            System.out.println("j=" + j);
            CustiomApplication customApplication = (CustiomApplication) context.getApplicationContext();
            Logger.d("customApplication:" + customApplication.getIsshowDown());
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

                    } else if (xml.get("trade_state").equals("USERPAYING")) {//用户正在付款
                        if (polling_times == 31) {
                            message.what = 1;
                            message.obj = "请重新下单";
                            handler.sendMessage(message);
                        } else
                            isPay = 0;
                    } else if (xml.get("trade_state").equals("REVOKED")) {//订单已撤销
                        isPay = 1;
                        message.what = 1;
                        message.obj = "订单已撤销";
                        handler.sendMessage(message);
                    } else if (xml.get("trade_state").equals("NOTPAY")) {//未付款
                        if (polling_times == 31) {
                            message.what = 1;
                            message.obj = "请重新下单";
                            handler.sendMessage(message);
                        } else
                            isPay = 0;
                    }
                } else {
                    if ((xml.get("err_code") != null && xml.get("err_code").equals("ACQ.TRADE_NOT_EXIST"))) {
                        isPay = 0;
                    } else {
                        message.what = 1;
                        message.obj = response.get();
                        handler.sendMessage(message);
                    }
                }
            }

            Logger.d(response.get());
        } else {
            //冲正
            message.what = 1;
            message.obj = "网络异常,请检查网络！";
            handler.sendMessage(message);
        }
    }


}
