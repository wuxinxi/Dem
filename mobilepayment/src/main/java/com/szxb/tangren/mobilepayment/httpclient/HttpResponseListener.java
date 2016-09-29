package com.szxb.tangren.mobilepayment.httpclient;

import android.content.Context;

import com.szxb.tangren.mobilepayment.dialog.WaitDialog;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

public class HttpResponseListener<T> implements OnResponseListener<T> {

    private Context context;

    private Request<T> request;

    private WaitDialog mDialog;

    private HttpListener<T> httpListener;

    /**
     * @param context
     * @param request   请求对象
     * @param callback  回调对象
     * @param canCancle 是否允许用户取消
     * @param isLoading 是否显示dialog
     */
    public HttpResponseListener(Context context, CharSequence msg, final Request<T> request,
                                HttpListener<T> callback, boolean canCancle, boolean isLoading) {
        this.context = context;
        this.request = request;
        if (context != null && isLoading) {
            mDialog = new WaitDialog(context, msg);
            mDialog.setCanceledOnTouchOutside(canCancle);
        }
        this.httpListener = callback;
    }

    public void onFinish(int arg0) {
        Logger.d("完成");
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public void onStart(int arg0) {
        Logger.i("开始");
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }
    }

    // 成功回调
    public void onSucceed(int what, Response<T> response) {
        int responseCode = response.getHeaders().getResponseCode();
        System.out.println("responseCode:" + responseCode + "");
        if (responseCode > 400 && context != null) {
            if (responseCode == 405) {
                Logger.d("服务器暂不支持该类型！");
            }
        }

        if (httpListener != null) {
            httpListener.success(what, response);
        }
    }

    @Override
    public void onFailed(int what, Response<T> response) {
        Logger.i("失败");
        if (httpListener != null)
            httpListener.fail(what, response);

    }


}
