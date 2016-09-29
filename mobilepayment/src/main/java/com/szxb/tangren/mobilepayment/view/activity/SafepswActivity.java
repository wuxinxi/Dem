package com.szxb.tangren.mobilepayment.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.szxb.tangren.mobilepayment.R;
import com.szxb.tangren.mobilepayment.presenter.SafepswCompl;
import com.szxb.tangren.mobilepayment.presenter.SafepswPresenter;
import com.szxb.tangren.mobilepayment.view.view.ResultView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/22 0022.
 */
public class SafepswActivity extends AppCompatActivity implements ResultView {
    @InjectView(R.id.mtoolbar)
    Toolbar mtoolbar;
    @InjectView(R.id.safePsw)
    EditText safePsw;
    @InjectView(R.id.num_del)
    Button numDel;
    @InjectView(R.id.determain)
    Button determain;
    @InjectView(R.id.linearLayoutNumkeyBoard)
    LinearLayout linearLayoutNumkeyBoard;

    private Button temp;

    private String psw;

    private String activity;

    private SafepswPresenter presenter;

    private int[] buttons = new int[]{R.id.num_0, R.id.num_1, R.id.num_2,
            R.id.num_3, R.id.num_4, R.id.num_5,
            R.id.num_6, R.id.num_7, R.id.num_8,
            R.id.num_9};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.safepsw_main);
        ButterKnife.inject(this);
        initView();

    }

    private void initView() {
        mtoolbar.setTitle("安全密码");
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity = getIntent().getStringExtra("activity");
        for (int i = 0; i < buttons.length; i++) {
            temp = (Button) findViewById(buttons[i]);
            temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    psw = safePsw.getText().toString();
                    psw = psw + String.valueOf(((Button) v).getText());
                    safePsw.setText(psw);
                }
            });
        }

        presenter = new SafepswCompl(this);

    }

    @OnClick({R.id.num_del, R.id.determain})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.num_del:
                delNum();
                break;
            case R.id.determain:
                presenter.onPost(this, safePsw);
                break;
        }
    }

    private void delNum() {
        String result = safePsw.getText().toString();
        if (result.equals(""))
            return;
        result = result.substring(0, result.length() - 1);
        safePsw.setText(result);
    }


    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onResult(int code, String result) {
        if (code == 100) {
            if (activity.equals("ManagerFragment"))
                startActivity(new Intent(SafepswActivity.this, PaySettingActivity.class));
            else if (activity.equals("BillActiviy"))
                startActivity(new Intent(SafepswActivity.this, RevokeActivity.class));
            overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
        } else {
            Toast.makeText(SafepswActivity.this, result, Toast.LENGTH_SHORT).show();
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
