package com.szxb.tangren.mobilepayment.presenter;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.szxb.tangren.mobilepayment.view.activity.WebViewActivity;

/**
 * Created by Administrator on 2016/9/28 0028.
 */
public class WebViewCompl implements WebViewPresenter {
    private static final String TAG = "WithDraActivity";

    private WebView webview;

    private ProgressBar progressBar;

    @Override
    public void onLogic(WebViewActivity context, WebView webView, ProgressBar progressBar, String url) {
        this.webview = webView;
        this.progressBar = progressBar;
        webview.getSettings().setJavaScriptEnabled(true);
        WebSettings settings = webview.getSettings();
        if (Build.VERSION.SDK_INT == 17) {
            settings.setDisplayZoomControls(false);
        }
        settings.setLoadWithOverviewMode(true);

        //分辨适应
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        switch (mDensity) {
            case 120:
                settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
                break;
            case 160:
                settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
                break;
            case 240:
                settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
                break;
            default:
                break;
        }
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        webview.setWebChromeClient(new myWebChromeClien());
        webview.setWebViewClient(new MyWebViewClient());
        webview.loadUrl(url);
    }

    class MyWebViewClient extends WebViewClient {
        public MyWebViewClient() {
            super();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d(TAG, "WebView Started,waitting");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //监听是否返回H5
            Log.d(TAG, "H5界面已返回到Activity");
            view.loadUrl(url);
            return true;
//            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d(TAG, "WebView Finished");
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            webview.loadUrl("");
            Log.d(TAG, "页面错误");
        }
    }


    class myWebChromeClien extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressBar.setProgress(newProgress);
            if (newProgress == 100)
                progressBar.setVisibility(View.GONE);

        }
    }

}
