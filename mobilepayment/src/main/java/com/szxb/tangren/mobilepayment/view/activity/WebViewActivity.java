package com.szxb.tangren.mobilepayment.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.szxb.tangren.mobilepayment.R;
import com.szxb.tangren.mobilepayment.presenter.WebViewCompl;
import com.szxb.tangren.mobilepayment.presenter.WebViewPresenter;

/**
 * Created by Administrator on 2016/9/28 0028.
 */
public class WebViewActivity extends AppCompatActivity {

    private WebViewPresenter presenter;

    private String url = "http://www.szxiaobing.cn:88/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_main);
        WebView webView = (WebView) findViewById(R.id.webView);
        ProgressBar progressB = (ProgressBar) findViewById(R.id.progressBar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.mtoolbar);
        presenter = new WebViewCompl();
        presenter.onLogic(this, webView, progressB, url);
        toolbar.setTitle("使用手册");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
