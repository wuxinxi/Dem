package com.szxb.tangren.mobilepayment.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.szxb.tangren.mobilepayment.R;
import com.szxb.tangren.mobilepayment.view.fragment.MainFragment;
import com.szxb.tangren.mobilepayment.view.fragment.ManagerFragment;
import com.szxb.tangren.mobilepayment.view.fragment.SettingFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.frameLayout)
    FrameLayout frameLayout;
    @InjectView(android.R.id.tabcontent)
    FrameLayout tabcontent;
    @InjectView(R.id.mTabHost)
    FragmentTabHost mTabHost;

    private LayoutInflater layoutInflater;

    private String mTextViewArray[] = {"主页", "管理", "设置"};

    private Class fragmentArray[] = {MainFragment.class,  ManagerFragment.class, SettingFragment.class};

    private int mImageViewArray[] = {R.drawable.main_selector, R.drawable.manager_selector, R.drawable.setting_selector};

    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        layoutInflater = LayoutInflater.from(this);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.frameLayout);
        mTabHost.getTabWidget().setDividerDrawable(null);//去除分割线
        for (int i = 0; i < 3; i++) {

            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextViewArray[i])
                    .setIndicator(getTabItemView(i));

            mTabHost.addTab(tabSpec, fragmentArray[i], null);
        }
    }

    private View getTabItemView(int index) {
        // TODO Auto-generated method stub
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextViewArray[index]);

        return view;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(getApplication(), "再按一次退出程序！", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
