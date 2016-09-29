package com.szxb.tangren.mobilepayment.presenter;

import android.content.Context;

/**
 * Created by Administrator on 2016/9/22 0022.
 */
public interface PaymentPresenter {
    /**
     * @param context
     * @param service      支付类型
     * @param amoutOfMoney 支付金额
     * @param out_trade_no 订单号
     */
    public void doSweptPay(Context context, String amoutOfMoney, String out_trade_no,
                           String payCode, String body, String service);

    public void doSweptCheckOrder(Context context, String out_trade_no);
}
