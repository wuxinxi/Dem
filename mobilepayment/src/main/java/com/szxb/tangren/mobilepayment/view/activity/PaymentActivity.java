package com.szxb.tangren.mobilepayment.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.szxb.tangren.mobilepayment.R;
import com.szxb.tangren.mobilepayment.presenter.PaymentCompl;
import com.szxb.tangren.mobilepayment.presenter.PaymentPresenter;
import com.szxb.tangren.mobilepayment.utils.Utils;
import com.szxb.tangren.mobilepayment.utils.singutils.XMlUtils;
import com.szxb.tangren.mobilepayment.view.view.PaymentView;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/22 0022.
 */
public class PaymentActivity extends AppCompatActivity implements PaymentView {

    @InjectView(R.id.mtoolbar)
    Toolbar mtoolbar;
    @InjectView(R.id.flashLamp)
    ImageView flashLamp;
    @InjectView(R.id.jianyi)
    TextView jianyi;
    @InjectView(R.id.fl_my_container)
    FrameLayout flMyContainer;
    @InjectView(R.id.image)
    ImageView image;
    @InjectView(R.id.mainsweep)
    Button mainsweep;
    @InjectView(R.id.checkOrder)
    Button checkOrder;
    private CaptureFragment captureFragment;

    private String payType;//支付方式

    private String payAnount;//支付金额

    private String out_trade_no;

    private PaymentPresenter paymentPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_main);
        ButterKnife.inject(this);

        captureFragment = new CaptureFragment();
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();

        initView();

        initPermission();//初始化权限，6.0以上
    }

    private void initPermission() {

    }

    public static boolean isOpen = false;

    private void initView() {
        jianyi.setText("建议与镜头保持10cm距离," + "\n" + "尽量避免逆光和光影");
        //得到支付方式与支付金额

        payType = getIntent().getStringExtra("payType");
        payAnount = getIntent().getStringExtra("amoumt");

        mtoolbar.setTitle(payType);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        paymentPresenter = new PaymentCompl(this);

    }

    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            image.setVisibility(View.VISIBLE);
            image.setImageBitmap(mBitmap);
            //小额支付即被扫（支付宝微信统一service）
            out_trade_no = Utils.OrderNo();
            paymentPresenter.doSweptPay(PaymentActivity.this, payAnount, out_trade_no, result, "商品", "unified.trade.micropay");
        }

        @Override
        public void onAnalyzeFailed() {
            Toast.makeText(PaymentActivity.this, "扫码失败！", Toast.LENGTH_SHORT).show();
        }
    };


    @OnClick({R.id.flashLamp, R.id.mainsweep, R.id.checkOrder})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.flashLamp:
                if (!isOpen) {
                    CodeUtils.isLightEnable(true);
                    isOpen = true;
                } else {
                    CodeUtils.isLightEnable(false);
                    isOpen = false;
                }
                break;
            case R.id.mainsweep:
                Intent intent = new Intent(PaymentActivity.this, MainSweepActivity.class);
                intent.putExtra("payType", payType);
                intent.putExtra("payAnount", payAnount);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.base_slide_remain, R.anim.base_slide_right_in);
                break;
            case R.id.checkOrder:
                if (out_trade_no == null)
                    Toast.makeText(PaymentActivity.this, "订单还未生成，请扫码！", Toast.LENGTH_SHORT).show();
                else
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
    public void onResult(int code, String result) {
        if (code == 100) {
            Map<String, String> xml = XMlUtils.decodeXml(result);

            String payType = xml.get("payType");
            String cashier = xml.get("cashier");
            String out_trade_no = xml.get("out_trade_no");
            String time_end = xml.get("time_end");
            String transaction_id = xml.get("transaction_id");

            Intent intent = new Intent(PaymentActivity.this, PayResultActivity.class);
            intent.putExtra("payType", payType);
            intent.putExtra("cashier", cashier);
            intent.putExtra("out_trade_no", out_trade_no);
            intent.putExtra("time_end", time_end);
            intent.putExtra("transaction_id", transaction_id);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
        } else if (code == 400) {
            Toast.makeText(PaymentActivity.this, result, Toast.LENGTH_LONG).show();
            finish();
            overridePendingTransition(0, R.anim.base_slide_right_out);
        } else if (code == 200) {
            Toast.makeText(PaymentActivity.this, result, Toast.LENGTH_LONG).show();
        } else if (code == 300) {
            Toast.makeText(PaymentActivity.this, result, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(PaymentActivity.this, result, Toast.LENGTH_LONG).show();
            finish();
            overridePendingTransition(0, R.anim.base_slide_right_out);
        }
    }

    @Override
    public void onCheckOrder(int code, String result) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }


}
