package com.szxb.tangren.mobilepayment.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.szxb.tangren.mobilepayment.R;
import com.szxb.tangren.mobilepayment.dialog.WaitDialog;
import com.szxb.tangren.mobilepayment.presenter.LoginCompl;
import com.szxb.tangren.mobilepayment.presenter.LoginPresenter;
import com.szxb.tangren.mobilepayment.utils.Sputils;
import com.szxb.tangren.mobilepayment.view.view.ResultView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/27 0027.
 */
public class Login extends AppCompatActivity implements ResultView {
    @InjectView(R.id.businessAccounts)
    EditText businessAccounts;
    @InjectView(R.id.user)
    EditText user;
    @InjectView(R.id.psw)
    EditText psw;
    @InjectView(R.id.isVisible)
    ImageView isVisible;
    @InjectView(R.id.login)
    Button login;
    @InjectView(R.id.rememberPsw)
    CheckBox rememberPsw;
    @InjectView(R.id.linear_layout)
    LinearLayout linearLayout;

    private boolean visible = false;

    private LoginPresenter presenter;

    private boolean isSelector;

    private Sputils spUtils;

    private WaitDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        WindowManager manager = this.getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int layoutWidth = (int) (width * 0.8);

        ViewGroup.LayoutParams lp = linearLayout.getLayoutParams();
        lp.width = layoutWidth;
        linearLayout.setLayoutParams(lp);

        presenter = new LoginCompl(this);
        spUtils = new Sputils();
        dialog = new WaitDialog(this, "正在登录请稍后");

        isSelector = (boolean) spUtils.get(this, "isSelector", false);
        rememberPsw.setChecked(isSelector);

        businessAccounts.setText((String) spUtils.get(this, "businessAccounts", ""));
        user.setText((String) spUtils.get(this, "user", ""));
        psw.setText((String) spUtils.get(this, "psw", ""));
    }

    @OnClick({R.id.isVisible, R.id.login, R.id.rememberPsw})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.isVisible:
                if (visible) {
                    //点击变成不可见
                    psw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isVisible.setBackgroundResource(R.mipmap.icon_no_visible);
                    visible = false;
                } else {
                    //点击变成可见
                    psw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isVisible.setBackgroundResource(R.mipmap.icon_visible);
                    visible = true;
                }
                break;
            case R.id.login:
                dialog.show();
                presenter.doLogin(Login.this, businessAccounts, user, psw, isSelector);
                break;
            case R.id.rememberPsw:
                isSelector = rememberPsw.isChecked();
                break;
        }
    }

    @Override
    public void onResult(int code, String result) {
        if (code == 400) {
            Toast.makeText(Login.this, result + "", Toast.LENGTH_SHORT).show();
            if (dialog.isShowing() && dialog != null) {
                dialog.dismiss();
            }
        } else {
            startActivity(new Intent(Login.this, MainActivity.class));
            overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dialog.isShowing() && dialog != null) {
            dialog.dismiss();
        }
    }
}
