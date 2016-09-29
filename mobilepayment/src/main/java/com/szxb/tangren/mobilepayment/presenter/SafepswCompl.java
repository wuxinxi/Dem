package com.szxb.tangren.mobilepayment.presenter;

import android.content.Context;
import android.widget.EditText;

import com.szxb.tangren.mobilepayment.utils.Sputils;
import com.szxb.tangren.mobilepayment.view.view.ResultView;

/**
 * Created by Administrator on 2016/9/28 0028.
 */
public class SafepswCompl implements SafepswPresenter {

    private ResultView view;

    private Sputils sputils;

    public SafepswCompl(ResultView view) {
        this.view = view;
    }

    @Override
    public void onPost(Context context, EditText psw) {
        sputils = new Sputils();
        String password = psw.getText().toString();
        if (password.equals(""))
            view.onResult(400, "安全密码不能为空！");
        else {
            if (password.equals(sputils.get(context, "safePsw", "666666")))
                view.onResult(100, "SUCCESS");
            else
                view.onResult(400, "密码错误！");
        }
    }
}
