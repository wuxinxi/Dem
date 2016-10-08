package com.szxb.tangren.mobilepayment.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.szxb.tangren.mobilepayment.R;
import com.szxb.tangren.mobilepayment.application.CustiomApplication;
import com.szxb.tangren.mobilepayment.presenter.MainSweepCompl;
import com.szxb.tangren.mobilepayment.presenter.MainSweepPresenter;
import com.szxb.tangren.mobilepayment.presenter.PaymentCompl;
import com.szxb.tangren.mobilepayment.presenter.PaymentPresenter;
import com.szxb.tangren.mobilepayment.utils.Config;
import com.szxb.tangren.mobilepayment.utils.Utils;
import com.szxb.tangren.mobilepayment.utils.singutils.XMlUtils;
import com.szxb.tangren.mobilepayment.view.view.MainSweepView;
import com.szxb.tangren.mobilepayment.view.view.PaymentView;
import com.yolanda.nohttp.Logger;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/24 0024.
 */
public class MainSweepActivity extends AppCompatActivity implements MainSweepView, PaymentView {
    @InjectView(R.id.mtoolbar)
    Toolbar mtoolbar;
    @InjectView(R.id.qrcode)
    ImageView qrcode;
    @InjectView(R.id.linear_qrcode)
    LinearLayout linearQrcode;
    @InjectView(R.id.swept)
    Button swept;
    @InjectView(R.id.checkOrder)
    Button checkOrder;

    private String payType;//支付方式

    private String payAnount;//支付金额

    private String out_trade_no;

    private MainSweepPresenter presenter;

    private PaymentPresenter paymentPresenter;

    private String service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainsweep_main);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        mtoolbar.setTitle("");
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WindowManager manager = this.getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        //屏幕宽度
        int width = metrics.widthPixels;
        //布局宽度2/width
        int layout = (int) (width * 0.7);
        ViewGroup.LayoutParams lp = linearQrcode.getLayoutParams();
        lp.height = layout;
        lp.width = layout;
        linearQrcode.setLayoutParams(lp);

        out_trade_no = Utils.OrderNo();
        presenter = new MainSweepCompl(this);
        paymentPresenter = new PaymentCompl(this);

        payType = getIntent().getStringExtra("payType");
        payAnount = getIntent().getStringExtra("payAnount");
        mtoolbar.setTitle(payType);

        if (payType.equals("微信"))
            service = Config.wechatService;
        else if (payType.equals("支付宝"))
            service = Config.aliService;
        else if (payType.equals("QQ"))
            service = Config.tenService;

        Logger.e("Thrad count:" + Thread.activeCount());

        presenter.doMainSweepPay(this, payAnount, out_trade_no, "商品", service);

    }

    @OnClick({R.id.swept, R.id.checkOrder})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.swept:
                Intent intent = new Intent(MainSweepActivity.this, PaymentActivity.class);
                intent.putExtra("payType", payType);
                intent.putExtra("amoumt", payAnount);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.base_slide_remain, R.anim.base_slide_right_in);
                break;
            case R.id.checkOrder:
                paymentPresenter.doSweptCheckOrder(this, out_trade_no);
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

    @Override
    protected void onStart() {
        super.onStart();
        CustiomApplication application = (CustiomApplication) getApplication();
        application.setIsshowDown(0);
    }

    @Override
    public void onUrlCode(String url_code) {
        Bitmap bitmap = null;
        try {
            bitmap = Utils.CreateCode(url_code);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        qrcode.setImageBitmap(bitmap);

    }

    @Override
    public void onResult(int code, String result) {
        if (code == 100) {
            Map<String, String> xml = XMlUtils.decodeXml(result);

            String payType = xml.get("payType");
            String cashier = xml.get("cashier");
            String out_trade_no = xml.get("out_trade_no");
            String time_end = xml.get("time_end");
            String transaction_id = xml.get("transaction_id");

            Intent intent = new Intent(MainSweepActivity.this, PayResultActivity.class);
            intent.putExtra("payType", payType);
            intent.putExtra("cashier", cashier);
            intent.putExtra("out_trade_no", out_trade_no);
            intent.putExtra("time_end", time_end);
            intent.putExtra("transaction_id", transaction_id);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
        } else if (code == 200) {
            Toast.makeText(MainSweepActivity.this, result, Toast.LENGTH_LONG).show();
        } else if (code == 300) {
            Toast.makeText(MainSweepActivity.this, result, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainSweepActivity.this, result, Toast.LENGTH_LONG).show();
            finish();
            overridePendingTransition(0, R.anim.base_slide_right_out);
        }

    }

    @Override
    public void onCheckOrder(int code, String result) {

    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustiomApplication application = (CustiomApplication) getApplication();
        application.setIsshowDown(1);
    }
}
