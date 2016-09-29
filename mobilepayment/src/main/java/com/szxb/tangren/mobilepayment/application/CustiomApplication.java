package com.szxb.tangren.mobilepayment.application;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;

/**
 * Created by Administrator on 2016/9/20 0020.
 */
public class CustiomApplication extends Application {

    private int isshowDown;

    @Override
    public void onCreate() {
        super.onCreate();
        NoHttp.initialize(this);
        Logger.setDebug(true);
        ActiveAndroid.initialize(this);
        setIsshowDown(0);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    public int getIsshowDown() {
        return isshowDown;
    }

    public void setIsshowDown(int isshowDown) {
        this.isshowDown = isshowDown;
    }
}
