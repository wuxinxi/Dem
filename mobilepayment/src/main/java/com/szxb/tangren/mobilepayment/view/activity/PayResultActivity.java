package com.szxb.tangren.mobilepayment.view.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.szxb.tangren.mobilepayment.R;
import com.szxb.tangren.mobilepayment.db.OrderDBManager;
import com.szxb.tangren.mobilepayment.utils.Utils;
import com.yolanda.nohttp.Logger;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/24 0024.
 */
public class PayResultActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    @InjectView(R.id.payAmount)
    TextView payAmount;
    @InjectView(R.id.mToolbar)
    Toolbar mToolbar;
    @InjectView(R.id.cashier)
    TextView cashier;
    @InjectView(R.id.shishou)
    TextView shishou;
    @InjectView(R.id.determin)
    Button determin;
    @InjectView(R.id.coll)
    CollapsingToolbarLayout coll;
    @InjectView(R.id.appBar)
    AppBarLayout appBar;

    private String payType;

    private String anount;

    private String out_trade_no;

    private String time_end;

    private String transaction_id;

    private OrderDBManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payresult_main);
        ButterKnife.inject(this);
        initView();

    }

    private void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        coll.setTitleEnabled(false);
        appBar.addOnOffsetChangedListener(this);
        manager = new OrderDBManager();

        anount = getIntent().getStringExtra("cashier");
        out_trade_no = getIntent().getStringExtra("out_trade_no");
        time_end = getIntent().getStringExtra("time_end");
        transaction_id = getIntent().getStringExtra("transaction_id");
        payType = getIntent().getStringExtra("payType");

        Logger.d(anount + "," + out_trade_no + "," + time_end + "," + transaction_id + "," + payType + "," + Utils.getDate());
        Logger.d(anount + "," + out_trade_no + "," + time_end + "," + transaction_id + "," + payType + "," + Utils.getDate());

        payAmount.setText(anount);
        cashier.setText(anount);
        shishou.setText(anount);


        try {
            manager.addRecord(out_trade_no, time_end, transaction_id, anount, payType, Utils.getDate());
        } catch (Exception e) {
            Toast.makeText(PayResultActivity.this, "订单保存失败！", Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.determin)
    public void onClick() {
        finish();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        Log.i("offset:", verticalOffset + "");
        if (verticalOffset == 0) {
            mToolbar.setTitle("");
        } else {
            mToolbar.setTitle(payType);
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
}
