package com.szxb.tangren.mobilepayment.view.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.szxb.tangren.mobilepayment.R;
import com.szxb.tangren.mobilepayment.db.OrderDBManager;
import com.szxb.tangren.mobilepayment.utils.excelutls.ExprotCsv;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/27 0027.
 */
public class ExportbillActivity extends AppCompatActivity {
    @InjectView(R.id.mtoolbar)
    Toolbar mtoolbar;
    @InjectView(R.id.today_bill)
    LinearLayout todayBill;
    @InjectView(R.id.yesterday_bill)
    LinearLayout yesterdayBill;
    @InjectView(R.id.thisMouth_bill)
    LinearLayout thisMouthBill;
    @InjectView(R.id.nextMouth_bill)
    LinearLayout nextMouthBill;
    @InjectView(R.id.all_bill)
    LinearLayout allBill;

    private OrderDBManager manager;

    private ExprotCsv csv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exportbill_main);
        ButterKnife.inject(this);
        initView();

    }

    private void initView() {
        mtoolbar.setTitle("账单下载");
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        manager = new OrderDBManager();
        csv = new ExprotCsv(this);
    }

    @OnClick({R.id.today_bill, R.id.yesterday_bill, R.id.thisMouth_bill, R.id.nextMouth_bill, R.id.all_bill})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.today_bill:
                Snackbar.make(mtoolbar, "暂仅支持导出全部账单", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.yesterday_bill:
                Snackbar.make(mtoolbar, "暂仅支持导出全部账单", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.thisMouth_bill:
                Snackbar.make(mtoolbar, "暂仅支持导出全部账单", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.nextMouth_bill:
                Snackbar.make(mtoolbar, "暂仅支持导出全部账单", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.all_bill:
                csv.saveCsv();
                break;
            default:
                break;
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
