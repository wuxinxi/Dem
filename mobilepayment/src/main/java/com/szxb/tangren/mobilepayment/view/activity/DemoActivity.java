package com.szxb.tangren.mobilepayment.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.szxb.tangren.mobilepayment.R;
import com.szxb.tangren.mobilepayment.adapter.DemoAdapter;
import com.szxb.tangren.mobilepayment.model.DemoBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/9/23 0023.
 */
public class DemoActivity extends AppCompatActivity {
    @InjectView(R.id.base_swipe_list)
    RecyclerView baseSwipeList;

    private static DemoAdapter adapter;

    static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 1:
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    private List<DemoBean> list = new ArrayList<DemoBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_main);
        ButterKnife.inject(this);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
                handler.sendEmptyMessage(1);
            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add2();
                handler.sendEmptyMessage(1);
            }
        });


        list = list();
        adapter = new DemoAdapter(DemoActivity.this, list);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        baseSwipeList.setLayoutManager(manager);
        baseSwipeList.setAdapter(adapter);

    }


    private List<DemoBean> list() {

        for (int i = 0; i < 3; i++) {
            DemoBean bean = new DemoBean();
            bean.setId(i);
            bean.setData("2016-08-01");
            bean.setTitle("我是序列号：" + i);
            list.add(bean);
        }

        return list;
    }


    private List<DemoBean> add() {
        for (int i = 0; i < 3; i++) {
            DemoBean bean = new DemoBean();
            bean.setId(i);
            bean.setData("2016-08-03");
            bean.setTitle("序列号：" + i);
            list.add(0, bean);
        }
        return list;
    }
private List<DemoBean> add2() {
        for (int i = 0; i < 3; i++) {
            DemoBean bean = new DemoBean();
            bean.setId(i);
            bean.setData("2016-08-07");
            bean.setTitle("序列号：" + i);
            list.add(0, bean);
        }
        return list;
    }

}
