package com.szxb.tangren.mobilepayment.presenter;

import android.content.Context;

/**
 * Created by Administrator on 2016/9/24 0024.
 */
public interface MainSweepPresenter {
    public void doMainSweepPay(Context context, String amoutOfMoney,
                               String out_trade_no, String body, String service);

}
