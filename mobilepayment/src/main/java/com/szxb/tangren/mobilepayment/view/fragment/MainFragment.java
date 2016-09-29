package com.szxb.tangren.mobilepayment.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szxb.tangren.mobilepayment.R;
import com.szxb.tangren.mobilepayment.db.OrderDBManager;
import com.szxb.tangren.mobilepayment.model.RecordBean;
import com.szxb.tangren.mobilepayment.utils.Utils;
import com.szxb.tangren.mobilepayment.view.activity.BillActivity;
import com.szxb.tangren.mobilepayment.view.activity.ReceiptActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/20 0020.
 */
public class MainFragment extends Fragment {
    @InjectView(R.id.today_count)
    TextView todayCount;
    @InjectView(R.id.linearLayout_pay)
    LinearLayout linearLayoutPay;
    @InjectView(R.id.linearLayout_bill)
    LinearLayout linearLayoutBill;
    @Nullable

    private View rootView;

    private OrderDBManager orderDBManager;

    private List<RecordBean> list = new ArrayList<RecordBean>();

    private double money = 0.00;

    private double sumMoney = 0.00;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_main, null);
            ButterKnife.inject(this, rootView);
            initView();
        }

        ViewGroup group = (ViewGroup) rootView.getParent();
        if (group != null) {
            group.removeView(rootView);
        }
        return rootView;
    }

    private void initView() {
        WindowManager manager = getActivity().getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        //屏幕宽度
        int width = metrics.widthPixels;
        //布局宽度2/width
        int layoutWidth = width / 2;
        ViewGroup.LayoutParams lp = linearLayoutPay.getLayoutParams();
        lp.height = layoutWidth;
        linearLayoutPay.setLayoutParams(lp);
        ViewGroup.LayoutParams lp2 = linearLayoutBill.getLayoutParams();
        lp2.height = layoutWidth;
        linearLayoutBill.setLayoutParams(lp2);
        orderDBManager = new OrderDBManager();

        getData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private void getData() {
        list = orderDBManager.getMoney(Utils.getDate());
        for (int i = 0; i < list.size(); i++) {
            RecordBean bean = list.get(i);
            money = Double.valueOf(bean.getMoney());
            Log.d("TAG", money + "元");
            sumMoney = sumMoney + money;
        }

        todayCount.setText(sumMoney + "元");

    }

    @OnClick({R.id.linearLayout_pay, R.id.linearLayout_bill})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linearLayout_pay:
                startActivity(new Intent(getActivity(), ReceiptActivity.class));
                getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
                break;
            case R.id.linearLayout_bill:
//
//                OrderDBManager manager = new OrderDBManager();
//
//                manager.addRecord("P2013123123156", "2016-06-09 12:50:60", "P2016121212506", "443", "微信支付", "2016-08-26");
//                manager.addRecord("P2013123123156", "2016-06-09 16:45:60", "P2016121216456", "786", "QQ支付", "2016-08-26");
//
//                manager.addRecord("P2013123123156", "2016-08-16 09:50:60", "P2016121212506", "100", "微信支付", "2016-08-27");
//                manager.addRecord("P20160920", "2016-08-16 10:45:60", "P2016121216456", "800", "支付宝支付", "2016-09-20");
//                manager.addRecord("P827", "2016-08-16 12:50:60", "P2016121212506", "700", "微信支付", "2016-09-20");
//
//                manager.addRecord("P345678", "2016-02-12 12:45:60", "P345678", "6832", "微信支付", "2016-09-27");
//                manager.addRecord("P234567", "2016-02-12 12:50:60", "P234567", "683", "微信支付", "2016-09-29");
//                manager.addRecord("P123456", "2016-02-12 16:45:60", "P123456", "7895", "支付宝支付", "2016-09-28");

//
//                manager.addRecord("P2013123123156", "2016-10-16 15:45:60", "P2016121216456", "900", "支付宝支付", "2016-09-27");
//                manager.addRecord("P2013123123156", "2016-10-16 18:50:60", "P2016121212506", "711", "微信支付", "2016-09-28");
//                manager.addRecord("P2013123123156", "2016-10-16 20:45:60", "P2016121216456", "1000", "支付宝支付", "2016-09-28");

                startActivity(new Intent(getActivity(), BillActivity.class));
                getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();
        list = orderDBManager.getMoney(Utils.getDate());
        sumMoney = 0.00;
        todayCount = (TextView) rootView.findViewById(R.id.today_count);
        for (int i = 0; i < list.size(); i++) {
            RecordBean bean = list.get(i);
            money = Double.valueOf(bean.getMoney());
            Log.d("TAG", money + "元");
            sumMoney = sumMoney + money;
        }

        todayCount.setText(sumMoney + "元");
    }
}
