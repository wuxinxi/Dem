package com.szxb.tangren.mobilepayment.utils;

/**
 * Created by Administrator on 2016/9/22 0022.
 */
public class NoDoubleClick {
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
