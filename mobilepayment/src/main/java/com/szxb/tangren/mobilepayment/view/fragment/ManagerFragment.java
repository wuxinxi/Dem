package com.szxb.tangren.mobilepayment.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szxb.tangren.mobilepayment.R;
import com.szxb.tangren.mobilepayment.view.activity.ExportbillActivity;
import com.szxb.tangren.mobilepayment.view.activity.SafepswActivity;
import com.szxb.tangren.mobilepayment.view.activity.UpdateSafePswActivity;
import com.szxb.tangren.mobilepayment.view.activity.WebViewActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/22 0022.
 */
public class ManagerFragment extends Fragment {

    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.mtoolbar)
    Toolbar mtoolbar;
    @InjectView(R.id.safePassword)
    LinearLayout safePassword;
    @InjectView(R.id.paySetting)
    LinearLayout paySetting;
    @InjectView(R.id.userInfo)
    LinearLayout userInfo;
    @InjectView(R.id.export_bill)
    LinearLayout exportBill;
    @InjectView(R.id.newGuidelines)
    LinearLayout newGuidelines;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.manager_fragment_view, null);
            ButterKnife.inject(this, rootView);
            initView();
        }
        ViewGroup group = (ViewGroup) rootView.getParent();
        if (group != null)
            group.removeView(rootView);

        ButterKnife.inject(this, rootView);
        return rootView;
    }

    private void initView() {
        title.setText("管理");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.safePassword, R.id.paySetting, R.id.userInfo, R.id.export_bill, R.id.newGuidelines})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.safePassword:
                startActivity(new Intent(getActivity(), UpdateSafePswActivity.class));
                getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
                break;
            case R.id.paySetting:
                Intent intent = new Intent(getActivity(), SafepswActivity.class);
                intent.putExtra("activity", "ManagerFragment");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
                break;
            case R.id.userInfo:
                break;
            case R.id.export_bill:
                startActivity(new Intent(getActivity(), ExportbillActivity.class));
                getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
                break;
            case R.id.newGuidelines:
                startActivity(new Intent(getActivity(), WebViewActivity.class));
                getActivity().overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
                break;
        }
    }
}
