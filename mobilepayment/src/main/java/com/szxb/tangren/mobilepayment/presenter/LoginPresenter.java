package com.szxb.tangren.mobilepayment.presenter;

import android.content.Context;
import android.widget.EditText;

/**
 * Created by Administrator on 2016/9/27 0027.
 */
public interface LoginPresenter {
    public void doLogin(Context context,EditText businessAccounts, EditText user, EditText psw, boolean select);
}
