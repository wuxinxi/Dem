package com.szxb.tangren.mobilepayment.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.szxb.tangren.mobilepayment.R;
import com.szxb.tangren.mobilepayment.utils.NoDoubleClick;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/21 0021.
 */
public class PlaceorderActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {


    @InjectView(R.id.countAmout)
    TextView countAmout;
    @InjectView(R.id.mToolbar)
    Toolbar mToolbar;
    @InjectView(R.id.coll)
    CollapsingToolbarLayout coll;
    @InjectView(R.id.appBar)
    AppBarLayout appBar;
    @InjectView(R.id.cashier)
    TextView cashier;
    @InjectView(R.id.shishou)
    TextView shishou;
    @InjectView(R.id.confirmOrder)
    Button confirmOrder;

    private String payType;//支付方式

    private String payAnount;//支付金额

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.placeorder_main);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {

        payType = getIntent().getStringExtra("payType");
        payAnount = getIntent().getStringExtra("amoumt");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        coll.setTitleEnabled(false);
        appBar.addOnOffsetChangedListener(this);

        countAmout.setText(payAnount);
        cashier.setText(payAnount);
        shishou.setText(payAnount);

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

    @OnClick(R.id.confirmOrder)
    public void onClick() {
        //防止点击过快造成重复下单
        if (NoDoubleClick.isFastDoubleClick())
            return;
        else {
            Intent intent = new Intent(PlaceorderActivity.this, PaymentActivity.class);
            intent.putExtra("payType", payType);
            intent.putExtra("amoumt", payAnount);
            intent.putExtra("activity", "PlaceorderActivity");
            startActivity(intent);
            overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
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
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }
}
