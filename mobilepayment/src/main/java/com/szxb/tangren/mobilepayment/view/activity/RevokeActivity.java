package com.szxb.tangren.mobilepayment.view.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.szxb.tangren.mobilepayment.R;
import com.szxb.tangren.mobilepayment.presenter.RevokeCompl;
import com.szxb.tangren.mobilepayment.presenter.RevokePresenter;
import com.szxb.tangren.mobilepayment.utils.Constant;
import com.szxb.tangren.mobilepayment.utils.Utils;
import com.szxb.tangren.mobilepayment.view.view.NotifiView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/24 0024.
 */
public class RevokeActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener, NotifiView {
    @InjectView(R.id.amout)
    TextView amout;
    @InjectView(R.id.mToolbar)
    Toolbar mToolbar;
    @InjectView(R.id.coll)
    CollapsingToolbarLayout coll;
    @InjectView(R.id.appBar)
    AppBarLayout appBar;
    @InjectView(R.id.timeend)
    TextView timeend;
    @InjectView(R.id.liushuihao)
    TextView liushuihao;
    @InjectView(R.id.payType)
    TextView payType;
    @InjectView(R.id.confirmOrder)
    Button confirmOrder;

    private RevokePresenter presenter;

    private int postion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.revoke_main);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        coll.setTitleEnabled(false);
        appBar.addOnOffsetChangedListener(this);

        presenter = new RevokeCompl(this);

        amout.setText(Constant.amount);
        timeend.setText(Constant.timeend);
        liushuihao.setText(Constant.liushuihaob);
        payType.setText(Constant.paytype);
        postion = Constant.postion;
    }

    @OnClick(R.id.confirmOrder)
    public void onClick() {
        presenter.onRevoke(this, liushuihao.getText().toString(), amout.getText().toString(), postion);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            mToolbar.setTitle("");
        } else {
            mToolbar.setTitle("撤销");
        }
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
    public void onResult(int code, String result, int postion) {
        if (code == 100) {
            Toast.makeText(RevokeActivity.this, "申请退款成功,将在1-3工作日将退款退到您的账户！", Toast.LENGTH_SHORT).show();
            Utils.Intent(this, "update", postion);
            finish();
            overridePendingTransition(0, R.anim.base_slide_right_out);
        } else {
            Toast.makeText(RevokeActivity.this, result, Toast.LENGTH_LONG).show();
            finish();
            overridePendingTransition(0, R.anim.base_slide_right_out);
        }
    }
}
