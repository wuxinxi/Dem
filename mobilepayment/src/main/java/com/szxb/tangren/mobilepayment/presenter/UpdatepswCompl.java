package com.szxb.tangren.mobilepayment.presenter;

import android.content.Context;
import android.widget.EditText;

import com.szxb.tangren.mobilepayment.utils.Sputils;
import com.szxb.tangren.mobilepayment.view.view.ResultView;

/**
 * Created by Administrator on 2016/9/28 0028.
 */
public class UpdatepswCompl implements UpdatepswPresenter {

    private ResultView view;

    private Sputils sputils;

    public UpdatepswCompl(ResultView view) {
        this.view = view;
    }

    @Override
    public void doUpdate(Context context, EditText psw, EditText newpsw) {
        sputils = new Sputils();
        String p = psw.getText().toString();
        String np = newpsw.getText().toString();
        if (p.equals("") || np.equals("")) {
            view.onResult(400, "参数不得缺省！");
        } else {
            if (!p.equals(sputils.get(context, "safePsw", "666666"))) {
                psw.setText("");
                view.onResult(400, "原密码有误，请重新输入！");
            } else {
                sputils.put(context, "safePsw", newpsw.getText().toString());
                view.onResult(100, "修改成功！");
            }
        }
    }
}
