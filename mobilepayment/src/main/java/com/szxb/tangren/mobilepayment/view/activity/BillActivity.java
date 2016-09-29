package com.szxb.tangren.mobilepayment.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.szxb.tangren.mobilepayment.R;
import com.szxb.tangren.mobilepayment.adapter.BillAdapter;
import com.szxb.tangren.mobilepayment.db.OrderDBManager;
import com.szxb.tangren.mobilepayment.inteface.CustomClickListener;
import com.szxb.tangren.mobilepayment.inteface.CustomItemLongClickListener;
import com.szxb.tangren.mobilepayment.model.RecordBean;
import com.szxb.tangren.mobilepayment.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/23 0023.
 */
public class BillActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, CustomClickListener, CustomItemLongClickListener {


    @InjectView(R.id.mtoolbar)
    Toolbar mtoolbar;
    @InjectView(R.id.linear_layout)
    LinearLayout linearLayout;


    private static RecyclerView recyclerView;

    private static SwipeRefreshLayout refreshLayout;

    private static BillAdapter mAdapter;

    private static List<RecordBean> mDataList = new ArrayList<RecordBean>();

    private OrderDBManager manager;


    private LocalBroadcastManager localBroadcastManager;

    private BroadcastReceiver receiver;


    static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    refreshLayout.setRefreshing(false);
                    break;
                case 1:
                    mAdapter.notifyDataSetChanged();
                    if (mDataList.size() != 0)
                        recyclerView.smoothScrollToPosition(0);
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_main_normal);
        ButterKnife.inject(this);
        initView();
        onrefreshInte();
    }

    private void onrefreshInte() {
        localBroadcastManager = LocalBroadcastManager
                .getInstance(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("update");
        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                int postion = intent.getIntExtra("postion", 0);
                mDataList.remove(postion);
                mAdapter.notifyDataSetChanged();
            }
        };
        localBroadcastManager.registerReceiver(receiver, filter);

    }

    private void initView() {
        mtoolbar.setTitle("账单");
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_green_light);

        refreshLayout.setOnRefreshListener(this);
        manager = new OrderDBManager();

        mDataList = getRecordData();
        mAdapter = new BillAdapter(this, mDataList, linearLayout);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setClickListener(this);
        mAdapter.setLongClickListener(this);

    }


    private List<RecordBean> getRecordData() {
        mDataList = manager.query();
        return mDataList;
    }

    private List<RecordBean> list() {
        for (int i = 0; i < 3; i++) {
            RecordBean bean = new RecordBean();
            bean.setTimeend("2014-12-12 12:45:60");
            bean.setTime("2016-02-02");
            bean.setPaytype("微信支付");
            bean.setMoney("0.02");
            bean.setOuttradeno("P2013123123156");
            mDataList.add(bean);

        }

        for (int i = 0; i < 3; i++) {
            RecordBean bean = new RecordBean();
            bean.setTimeend("2014-12-12 12:45:60");
            bean.setTime("2016-02-04");
            bean.setPaytype("支付宝支付");
            bean.setMoney("0.04");
            bean.setOuttradeno("P2013123123156");
            mDataList.add(0, bean);
        }
        for (int i = 0; i < 3; i++) {
            RecordBean bean = new RecordBean();
            bean.setTimeend("2014-12-12 12:45:60");
            bean.setTime("2016-02-08");
            bean.setPaytype("QQ支付");
            bean.setMoney("0.04");
            bean.setOuttradeno("P2013123123156");
            mDataList.add(0, bean);
        }

        return mDataList;
    }


    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void setClickListtener(View view, int postion) {
    }


    @OnClick(R.id.mtoolbar)
    public void onClick() {
        recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void setItemLongClick(View view, int postion) {
        RecordBean bean = mDataList.get(postion);
        Intent intent = new Intent(BillActivity.this, SafepswActivity.class);
        intent.putExtra("activity", "BillActiviy");
        Constant.amount = bean.getMoney();
        Constant.liushuihaob = bean.getOuttradeno();
        Constant.paytype = bean.getPaytype();
        Constant.timeend = bean.getTimeend();
        Constant.postion = postion;
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bill, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(0, R.anim.base_slide_right_out);
                break;
            case R.id.search:
                Intent intent = new Intent(BillActivity.this, QueryOrderActivity.class);
                startActivityForResult(intent, 0);
                overridePendingTransition(R.anim.base_slide_remain, R.anim.base_slide_right_in);
//                Logger.d("开始查询了");
//                mDataList = manager.getYesterdayReocrd();
//                for (int i = 0; i < mDataList.size(); i++) {
//                    RecordBean bean = mDataList.get(i);
//                    Logger.d(bean.getOuttradeno());
//                }
//
//                Log.d("mlist的长度", mDataList.size() + "");

                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (manager != null)
            localBroadcastManager.unregisterReceiver(receiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {


            String payType = data.getStringExtra("payType");
            String startTime = data.getStringExtra("startTime");
            String endTime = data.getStringExtra("endTime");
            mDataList.clear();
            if (payType.equals("全部") && startTime.equals("") && endTime.equals("")) {
                mDataList.addAll(manager.query());
            } else if (payType.equals("全部") && !startTime.equals("") && endTime.equals("")) {
                mDataList.addAll(manager.queryNo(startTime));
            } else if (payType.equals("全部") && !startTime.equals("") && !endTime.equals("")) {
                mDataList.addAll(manager.startTimeToendTime(startTime, endTime));
            } else if (!payType.equals("全部") && !startTime.equals("") && !endTime.equals("")) {
                mDataList.addAll(manager.queryTypeAndtime(payType, startTime, endTime));
            } else if (!payType.equals("全部") && !startTime.equals("") && endTime.equals("")) {
                mDataList.addAll(manager.queryTypeAndtime(payType, startTime));
            } else if (!payType.equals("全部") && startTime.equals("") && endTime.equals("")) {
                mDataList.addAll(manager.queryPaytype(payType));
            }
            handler.sendEmptyMessage(1);
        }
    }

}
