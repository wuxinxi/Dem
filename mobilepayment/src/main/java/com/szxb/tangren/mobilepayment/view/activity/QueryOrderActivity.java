package com.szxb.tangren.mobilepayment.view.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.activeandroid.util.Log;
import com.squareup.timessquare.CalendarPickerView;
import com.szxb.tangren.mobilepayment.R;
import com.szxb.tangren.mobilepayment.utils.NoDoubleClick;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/24 0024.
 */
public class QueryOrderActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    @InjectView(R.id.info)
    TextView info;
    @InjectView(R.id.mToolbar)
    Toolbar mToolbar;
    @InjectView(R.id.coll)
    CollapsingToolbarLayout coll;
    @InjectView(R.id.appBar)
    AppBarLayout appBar;
    @InjectView(R.id.spiner)
    AppCompatSpinner spiner;
    @InjectView(R.id.startTime)
    TextView startTime;
    @InjectView(R.id.endTime)
    TextView endTime;
    @InjectView(R.id.query)
    Button query;
    @InjectView(R.id.spinerTime)
    AppCompatSpinner spinerTime;

    private boolean selectStart = true;
    private static final String TAG = "SampleTimesSquareActivity";
    private CalendarPickerView calendar;
    private AlertDialog theDialog;
    private CalendarPickerView dialogView;
    private Calendar lastYear;
    private Calendar nextYear;

    private String payType = "全部";
    private String timeRanges = "今天";
    public String DATEPICKER_TAG = "datepicker";

    private String title = "请选择";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_order_main);
        ButterKnife.inject(this);

        initView();

    }

    private void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        coll.setTitleEnabled(false);
        appBar.addOnOffsetChangedListener(this);
        info.setText("可根据交易类型、交易时间" + "\n" + "进行查询");

        spinnerData();


        calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -1);

        nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        calendar.init(lastYear.getTime(), nextYear.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.SINGLE) //
                .withSelectedDate(new Date());

    }

    private void spinnerData() {
        final String items[] = getResources().getStringArray(R.array.paytype);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner.setAdapter(adapter);
        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                payType = items[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final String timeRange[] = getResources().getStringArray(R.array.timeRange);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, timeRange);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerTime.setAdapter(arrayAdapter);
        spinerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timeRanges = timeRange[position];
                if (!timeRanges.equals("请选择时间范围")) {
                    startTime.setEnabled(false);
                    startTime.setBackgroundResource(R.drawable.edit_shape_1_1);
                    endTime.setEnabled(false);
                    endTime.setBackgroundResource(R.drawable.edit_shape_1_1);
                } else {
                    startTime.setEnabled(true);
                    startTime.setBackgroundResource(R.drawable.edit_shape_1);
                    endTime.setEnabled(true);
                    endTime.setBackgroundResource(R.drawable.edit_shape_1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @OnClick({R.id.startTime, R.id.endTime, R.id.query})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startTime:
                if (NoDoubleClick.isFastDoubleClick())
                    return;
                else {
                    selectStart = true;
                    showCalendarInDialog(title, R.layout.date_dialog);
                    dialogView.init(lastYear.getTime(), nextYear.getTime(), new Locale("zh", "cn")) //语言
                            .withSelectedDate(new Date());
                }
                break;
            case R.id.endTime:
                if (NoDoubleClick.isFastDoubleClick()) {
                    return;
                } else {
                    selectStart = false;
                    showCalendarInDialog(title, R.layout.date_dialog);
                    dialogView.init(lastYear.getTime(), nextYear.getTime(), new Locale("zh", "cn")) //语言
                            .withSelectedDate(new Date());
                }
                break;
            case R.id.query:
                Intent intent = new Intent();
                intent.putExtra("payType", payType);
                intent.putExtra("startTime", startTime.getText().toString());
                intent.putExtra("endTime", endTime.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(0, R.anim.base_slide_right_out);
                break;
        }
    }

    private void showCalendarInDialog(String title, int layoutResId) {
        dialogView = (CalendarPickerView) getLayoutInflater().inflate(layoutResId, null, false);

        theDialog = new AlertDialog.Builder(this) //
                .setTitle(title)
                .setView(dialogView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Date date = new Date(dialogView.getSelectedDate().getTime());
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        if (selectStart)
                            startTime.setText(format.format(date));
                        else
                            endTime.setText(format.format(date));
                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
        theDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Log.d(TAG, "onShow: fix the dimens!");
                dialogView.fixDialogDimens();
            }
        });
        theDialog.show();
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
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0)
            mToolbar.setTitle("");
        else
            mToolbar.setTitle("查询");

    }

}
