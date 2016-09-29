package com.szxb.tangren.mobilepayment.presenter;

import android.webkit.WebView;
import android.widget.ProgressBar;

import com.szxb.tangren.mobilepayment.view.activity.WebViewActivity;

/**
 * Created by Administrator on 2016/9/28 0028.
 */
public interface WebViewPresenter {
    void onLogic(WebViewActivity context, WebView webView, ProgressBar progressBar, String url);
}
