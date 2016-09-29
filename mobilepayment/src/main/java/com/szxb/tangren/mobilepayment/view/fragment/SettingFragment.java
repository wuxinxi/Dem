package com.szxb.tangren.mobilepayment.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szxb.tangren.mobilepayment.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/9/22 0022.
 */
public class SettingFragment extends Fragment {

    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.mtoolbar)
    Toolbar mtoolbar;
    @InjectView(R.id.editionno)
    LinearLayout editionno;
    @InjectView(R.id.newedition)
    LinearLayout newedition;
    @InjectView(R.id.phone)
    LinearLayout phone;
    @InjectView(R.id.out)
    LinearLayout out;
    private View rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.setting_fragment_view, null);
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
        title.setText("设置");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.editionno, R.id.newedition, R.id.phone, R.id.out})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editionno:
                break;
            case R.id.newedition:
                break;
            case R.id.phone:
                break;
            case R.id.out:
                getActivity().finish();
                break;
        }
    }
}
