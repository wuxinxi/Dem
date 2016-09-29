package com.szxb.tangren.mobilepayment.presenter;

import android.content.Context;

/**
 * Created by Administrator on 2016/9/24 0024.
 */
public interface RevokePresenter {
    public void onRevoke(Context context, String out_trade_no, String total_fee, int postion);
}
