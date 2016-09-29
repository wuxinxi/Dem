package com.szxb.tangren.mobilepayment.presenter;

import android.content.Context;

import com.szxb.tangren.mobilepayment.db.OrderDBManager;
import com.szxb.tangren.mobilepayment.httpclient.CallServer;
import com.szxb.tangren.mobilepayment.httpclient.HttpListener;
import com.szxb.tangren.mobilepayment.utils.Config;
import com.szxb.tangren.mobilepayment.utils.Parmas;
import com.szxb.tangren.mobilepayment.utils.singutils.XMlUtils;
import com.szxb.tangren.mobilepayment.view.view.NotifiView;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.CacheMode;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/24 0024.
 */
public class RevokeCompl implements RevokePresenter {

    private NotifiView view;

    private OrderDBManager manager;

    private int postion;

    public RevokeCompl(NotifiView view) {
        this.view = view;
        manager = new OrderDBManager();
    }

    @Override
    public void onRevoke(Context context, String out_trade_no, String total_fee, int postion) {
        this.postion = postion;
        Request<String> request = NoHttp.createStringRequest(Config.payUrl, RequestMethod.POST);
        request.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);
        request.setDefineRequestBodyForXML(Parmas.reFundArgs(context, out_trade_no, total_fee));
        CallServer.getHttpclient().add(context, "", 3, request, callBack, false, true);
    }

    HttpListener<String> callBack = new HttpListener<String>() {
        @Override
        public void success(int what, Response<String> response) {
            Map<String, String> xml = XMlUtils.decodeXml(response.get());
            System.out.println(xml.toString());
            int status = Integer.valueOf(xml.get("status"));
            if (status == 0) {
                int result_code = Integer.valueOf(xml.get("result_code"));
                if (result_code == 0) {
                    view.onResult(100, "SUCCESS", postion);
                    manager.delete(xml.get("out_trade_no"));
//                    recordBeans.remove(custom_postion);
//                    mAdapter.notifyDataSetChanged();
//                    Toast.makeToast(OrderActivity.this, "申请退款成功,将在1-3工作日将退款退到您的账户！", Toast.LENGTH_LONG).show();
                } else if (result_code == 1) {

                    view.onResult(400, response.get(), 0);
                }
            } else {
                view.onResult(400, response.get(), 0);
            }
        }

        @Override
        public void fail(int what, Response<String> response) {
            view.onResult(400, "网络异常,请重试！", 0);
        }
    };
}
