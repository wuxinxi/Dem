package com.szxb.tangren.mobilepayment.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.szxb.tangren.mobilepayment.R;
import com.szxb.tangren.mobilepayment.utils.Sputils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/22 0022.
 */
public class PaySettingActivity extends AppCompatActivity {
    @InjectView(R.id.mtoolbar)
    Toolbar mtoolbar;
    @InjectView(R.id.agent_type)
    RadioButton agentType;
    @InjectView(R.id.normal_type)
    RadioButton normalType;
    @InjectView(R.id.radio)
    RadioGroup radio;
    @InjectView(R.id.sign_agentno)
    EditText signAgentno;
    @InjectView(R.id.merchantId)
    EditText merchantId;
    @InjectView(R.id.agentno_key)
    EditText agentnoKey;
    @InjectView(R.id.qudaomoshi)
    LinearLayout qudaomoshi;
    @InjectView(R.id.mch_id)
    EditText mchId;
    @InjectView(R.id.mch_key)
    EditText mchKey;
    @InjectView(R.id.putongmoshi)
    LinearLayout putongmoshi;
    @InjectView(R.id.save)
    Button save;

    private Sputils sputils;

    //1:渠道模式，2：普通模式
    private int tradModel;

    private boolean select = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_setting_main);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        mtoolbar.setTitle("支付设置");
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sputils = new Sputils();

        //获取支付模式，默认模式为渠道模式
        tradModel = (int) sputils.get(this, "tradModel", 1);
        if (tradModel == 1) {
            agentType.setChecked(true);
            normalType.setChecked(false);
            qudaomoshi.setVisibility(View.VISIBLE);
            putongmoshi.setVisibility(View.GONE);
            signAgentno.setText(sputils.get(PaySettingActivity.this, "signAgentno", "105570000016").toString());
            merchantId.setText(sputils.get(PaySettingActivity.this, "merchantId", "100510021146").toString());
            agentnoKey.setText(sputils.get(PaySettingActivity.this, "agentnoKey", "1acef66a8122cc8b0bac219db862af50").toString());
        } else {
            agentType.setChecked(false);
            normalType.setChecked(true);
            putongmoshi.setVisibility(View.VISIBLE);
            qudaomoshi.setVisibility(View.GONE);
            mchId.setText(sputils.get(PaySettingActivity.this, "mchId", "105580000075").toString());
            mchKey.setText(sputils.get(PaySettingActivity.this, "mchKey", "c1b48bf94baed7757cb189301106e7a1").toString());
        }

        //模式默认为渠道模式
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //agentType=渠道模式
                if (checkedId == agentType.getId()) {
                    qudaomoshi.setVisibility(View.VISIBLE);
                    putongmoshi.setVisibility(View.GONE);
                    tradModel = 1;
                } else {
                    putongmoshi.setVisibility(View.VISIBLE);
                    qudaomoshi.setVisibility(View.GONE);
                    tradModel = 2;
                }
            }
        });

    }

    @OnClick(R.id.save)
    public void onClick() {
        if (tradModel == 1) {
            if (signAgentno.getText().toString().equals("")
                    || merchantId.getText().toString().equals("")
                    || agentnoKey.getText().toString().equals(""))
                Toast.makeText(PaySettingActivity.this, "参数不能缺省！", Toast.LENGTH_SHORT).show();
            else {
                sputils.put(this, "signAgentno", signAgentno.getText().toString().trim());
                sputils.put(this, "merchantId", merchantId.getText().toString().trim());
                sputils.put(this, "agentnoKey", agentnoKey.getText().toString().trim());
                sputils.put(this, "tradModel", 1);
                Toast.makeText(PaySettingActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(0, R.anim.base_slide_right_out);
            }
        } else {
            if (mchId.getText().toString().equals("") || mchKey.getText().toString().equals(""))
                Toast.makeText(PaySettingActivity.this, "参数不能缺省！", Toast.LENGTH_SHORT).show();
            else {
                sputils.put(this, "mchId", mchId.getText().toString().trim());
                sputils.put(this, "mchKey", mchKey.getText().toString().trim());
                sputils.put(this, "tradModel", 2);
                Toast.makeText(PaySettingActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(0, R.anim.base_slide_right_out);
            }
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
