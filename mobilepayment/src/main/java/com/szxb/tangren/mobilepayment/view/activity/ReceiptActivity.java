package com.szxb.tangren.mobilepayment.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.szxb.tangren.mobilepayment.R;
import com.szxb.tangren.mobilepayment.utils.popuwindow.SelectPicPopupWindow;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/21 0021.
 */
public class ReceiptActivity extends AppCompatActivity {
    @InjectView(R.id.mtoolbar)
    Toolbar mtoolbar;
    @InjectView(R.id.amount)
    EditText amount;
    @InjectView(R.id.pay_type)
    TextView payType;
    @InjectView(R.id.linearLayout_update_paytype)
    LinearLayout linearLayoutUpdatePaytype;
    @InjectView(R.id.num_7)
    Button num7;
    @InjectView(R.id.num_8)
    Button num8;
    @InjectView(R.id.num_9)
    Button num9;
    @InjectView(R.id.num_4)
    Button num4;
    @InjectView(R.id.num_5)
    Button num5;
    @InjectView(R.id.num_6)
    Button num6;
    @InjectView(R.id.clear)
    Button clear;
    @InjectView(R.id.num_1)
    Button num1;
    @InjectView(R.id.num_2)
    Button num2;
    @InjectView(R.id.num_3)
    Button num3;
    @InjectView(R.id.num_0)
    Button num0;
    @InjectView(R.id.num_del)
    Button numDel;
    @InjectView(R.id.determain)
    Button determain;

    private int count = 0;//标识输入字符数，用来判断长度是否超过0.00的长度
    private int index = 0; // 输入内容前的下标

    private SelectPicPopupWindow selectPicPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receipt_main);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        mtoolbar.setTitle("收款");
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        amount.setText("0.00");
        amount.setSelection(4);
        index = amount.getSelectionEnd();
        selectPicPopupWindow = new SelectPicPopupWindow(this, clickListener);
    }

    @OnClick({R.id.linearLayout_update_paytype, R.id.num_7, R.id.num_8, R.id.num_9, R.id.num_4, R.id.num_5, R.id.num_6, R.id.clear, R.id.num_1, R.id.num_2, R.id.num_3, R.id.num_0, R.id.num_del, R.id.determain})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linearLayout_update_paytype:
                View parent = ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
                selectPicPopupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.num_7:
                input("7");
                break;
            case R.id.num_8:
                input("8");
                break;
            case R.id.num_9:
                input("9");
                break;
            case R.id.num_4:
                input("4");
                break;
            case R.id.num_5:
                input("5");
                break;
            case R.id.num_6:
                input("6");
                break;
            case R.id.clear:
                amount.setText("0.00");
                break;
            case R.id.num_1:
                input("1");
                break;
            case R.id.num_2:
                input("2");
                break;
            case R.id.num_3:
                input("3");
                break;
            case R.id.num_0:
                input("0");
                break;
            case R.id.num_del:
                delNum();
                break;
            case R.id.determain:
                if (amount.getText().toString().equals("0.00")) {
                    Toast.makeText(ReceiptActivity.this, "请输入收款金额", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(ReceiptActivity.this, PlaceorderActivity.class);
                    intent.putExtra("payType", payType.getText().toString());
                    intent.putExtra("amoumt", amount.getText().toString());
                    startActivity(intent);
                    overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
                }
                break;
        }
    }

    private void input(String num) {
        String result = amount.getText().toString().trim() + num;
        result = result.replace(".", "");
        if (count < 3 && index == result.length() && result.startsWith("0")) {
            result = result.substring(1, result.length());
        }
        String a = String.valueOf(Long.valueOf(result.substring(0, result.length() - 2)));//整数部分
        StringBuilder sb = new StringBuilder(a);
        sb.append(".").append(result.subSequence(result.length() - 2, result.length()));//小数部分
        String tem = sb.toString();
        if (tem.length() > 10)
            return;
        amount.setText(tem);
        amount.setSelection(tem.length());
        count++;
    }

    private void delNum() {
        if (count != 0) {
            String result = amount.getText().toString().trim();

            result = result.replace(".", "");
            if (result.length() == 3) {
                result = "0".concat(result);
            }
            StringBuilder builder = new StringBuilder(result.substring(0, result.length() - 1));
            builder.insert(builder.toString().length() - 2, ".");

            amount.setText(builder.toString());
            amount.setSelection(builder.length());
            count--;
        } else {
            amount.setText("0.00");
            amount.setSelection(4);
        }
    }


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.wechat_pay:
                    payType.setText("微信");
                    break;
                case R.id.ali_pay:
                    payType.setText("支付宝");
                    break;
                case R.id.ten_pay:
                    payType.setText("QQ");
                    break;


            }
            selectPicPopupWindow.dismiss();
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(0, R.anim.base_slide_right_out);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        amount.setText("0.00");
    }

    @Override
    protected void onResume() {
        super.onResume();
        amount.setText("0.00");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }
}
