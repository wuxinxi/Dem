package com.szxb.tangren.mobilepayment.view.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.szxb.tangren.mobilepayment.R;
import com.szxb.tangren.mobilepayment.presenter.UpdatepswCompl;
import com.szxb.tangren.mobilepayment.presenter.UpdatepswPresenter;
import com.szxb.tangren.mobilepayment.view.view.ResultView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/28 0028.
 */
public class UpdateSafePswActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, ResultView {
    @InjectView(R.id.mToolbar)
    Toolbar mToolbar;
    @InjectView(R.id.coll)
    CollapsingToolbarLayout coll;
    @InjectView(R.id.appBar)
    AppBarLayout appBar;
    @InjectView(R.id.psw)
    EditText psw;
    @InjectView(R.id.newpsw)
    EditText newpsw;
    @InjectView(R.id.isVisible)
    ImageView isVisible;
    @InjectView(R.id.update)
    Button update;

    private boolean visible = false;

    private UpdatepswPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updatesafepsw_main);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        coll.setTitleEnabled(false);
        appBar.addOnOffsetChangedListener(this);
        presenter = new UpdatepswCompl(this);
    }

    @OnClick({R.id.isVisible, R.id.update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.isVisible:
                if (visible) {
                    //点击变成不可见
                    newpsw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isVisible.setBackgroundResource(R.mipmap.icon_no_visible);
                    visible = false;
                } else {
                    //点击变成可见
                    newpsw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isVisible.setBackgroundResource(R.mipmap.icon_visible);
                    visible = true;
                }
                break;
            case R.id.update:
                presenter.doUpdate(this, psw, newpsw);
                break;
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0)
            mToolbar.setTitle("");
        else
            mToolbar.setTitle("修改安全密码");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(0, R.anim.base_slide_right_out);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }

    @Override
    public void onResult(int code, String result) {
        if (code == 400)
            Toast.makeText(UpdateSafePswActivity.this, result, Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(UpdateSafePswActivity.this, result, Toast.LENGTH_SHORT).show();
            finish();
            overridePendingTransition(0, R.anim.base_slide_right_out);
        }
    }
}
