package com.szxb.tangren.mobilepayment.view.view;

/**
 * Created by Administrator on 2016/9/22 0022.
 */
public interface PaymentView {
    public void onResult(int code, String result);

    public void onCheckOrder(int code, String result);
}
