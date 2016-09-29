package com.szxb.tangren.mobilepayment.presenter;

import android.content.Context;
import android.widget.EditText;

import com.szxb.tangren.mobilepayment.utils.Sputils;
import com.szxb.tangren.mobilepayment.view.view.ResultView;

/**
 * Created by Administrator on 2016/9/27 0027.
 */
public class LoginCompl implements LoginPresenter {

    private ResultView view;

    private Sputils sputils;

    public LoginCompl(ResultView view) {
        this.view = view;
    }

    /**
     * @param businessAccounts 商户名称
     * @param user             商户账号
     * @param psw              商户密码
     * @param select           是否记住密码
     */
    @Override
    public void doLogin(Context context, EditText businessAccounts, EditText user, EditText psw, boolean select) {
        sputils = new Sputils();
        String ba = businessAccounts.getText().toString();
        String u = user.getText().toString();
        String p = psw.getText().toString();
        if (ba.equals("") || u.equals("") || p.equals("")) {
            view.onResult(400, "商户名称、账号或密码不能为空！");
        } else {
            //if true 保存密码
            if (select) {
                if (ba.equals("Test") && u.equals("1234567890") && p.equals("m1234")) {
                    sputils.put(context, "isSelector", true);
                    sputils.put(context, "businessAccounts", ba);
                    sputils.put(context, "user", u);
                    sputils.put(context, "psw", p);
                    view.onResult(100, "登录成功！");
                } else
                    view.onResult(400, "用户信息不匹配！");

            } else {
                if (ba.equals("Test") && u.equals("1234567890") && p.equals("m1234"))
                    view.onResult(100, "登录成功！");
                else
                    view.onResult(400, "用户信息不匹配！");

            }
        }
    }
}
