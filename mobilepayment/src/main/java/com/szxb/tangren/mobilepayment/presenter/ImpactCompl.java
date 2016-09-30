package com.szxb.tangren.mobilepayment.presenter;

import android.content.Context;

import com.szxb.tangren.mobilepayment.httpclient.CallServer;
import com.szxb.tangren.mobilepayment.httpclient.HttpListener;
import com.szxb.tangren.mobilepayment.utils.Config;
import com.szxb.tangren.mobilepayment.utils.Parmas;
import com.szxb.tangren.mobilepayment.utils.singutils.XMlUtils;
import com.szxb.tangren.mobilepayment.view.view.ImpactView;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.CacheMode;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import java.util.Map;

/**
 * Created by Administrator on 2016/9/30 0030.
 */
public class ImpactCompl implements ImpactPresetner {

    private ImpactView view;

    public ImpactCompl(ImpactView view) {
        this.view = view;
    }


    @Override
    public void doImpact(Context context, String out_trade_no) {
        Impact(context, out_trade_no);
    }

    private void Impact(Context context, String out_trade_no) {
        Request<String> request = NoHttp.createStringRequest(Config.payUrl, RequestMethod.POST);
        request.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);
        request.setDefineRequestBodyForXML(Parmas.Impact(context, out_trade_no));
        CallServer.getHttpclient().add(context, "", 5,
                request, impactCallBack, false, true);
    }

    HttpListener<String> impactCallBack = new HttpListener<String>() {
        @Override
        public void success(int what, Response<String> response) {
            Logger.d(response.get());
            Map<String, String> xml = XMlUtils.decodeXml(response.get());
            int status = Integer.valueOf(xml.get("status"));
            if (status == 0) {
                int result_code = Integer.valueOf(xml.get("result_code"));
                if (result_code == 0) {
                    view.onResult(100, "关单成功！");
                } else {
                    view.onResult(100, response.get());
                }
            }
        }

        @Override
        public void fail(int what, Response<String> response) {
            view.onResult(400, "网络异常");
        }
    };
}
