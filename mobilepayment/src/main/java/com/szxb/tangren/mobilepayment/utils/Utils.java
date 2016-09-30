package com.szxb.tangren.mobilepayment.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.support.v4.content.LocalBroadcastManager;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Administrator on 2016/9/1 0001.
 */
public class Utils {


    /**
     * 获取本机IP
     *
     * @return 下午3:35:02
     * @author TangRen
     */
    public static String localIp() {
        String ip = null;
        Enumeration allNetInterfaces;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces
                        .nextElement();
                List<InterfaceAddress> InterfaceAddress = netInterface
                        .getInterfaceAddresses();
                for (InterfaceAddress add : InterfaceAddress) {
                    InetAddress Ip = add.getAddress();
                    if (Ip != null && Ip instanceof Inet4Address) {
                        ip = Ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("获取本机Ip失败:异常信息:" + e.getMessage());
        }
        return ip;
    }

    /**
     * 生成二维码
     *
     * @param str
     * @return
     * @throws WriterException
     * @author TangRen
     * @time 2016-7-2
     */
    public static Bitmap CreateCode(String str) throws WriterException {
        Bitmap bitmap;
        BitMatrix matrix = new MultiFormatWriter().encode(str,
                BarcodeFormat.QR_CODE, 300, 300);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        // 二维矩阵转为一维像素数组,也就是一直横着排了
        int[] pix = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pix[y * width + x] = 0xff000000;
                }
            }
        }
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pix, 0, width, 0, 0, width, height);
        return bitmap;

    }

    /**
     * 获得订单号（当前时期+4位随机数）
     *
     * @return 下午3:38:07
     * @author TangRen
     */
    public static String OrderNo() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String dateString = format.format(date);// 当前系统时间
        int buildRandom = buildRandom(4);// 5位数的随机数
        return "P" + dateString + String.valueOf(buildRandom);
    }

    public static int buildRandom(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return (int) ((random * num));
    }

    /**
     * @param length
     * @return 随机字符串
     */
    public static String Random(int length) {
        char[] ss = new char[length];
        int i = 0;
        while (i < length) {
            int f = (int) (Math.random() * 5);
            if (f == 0)
                ss[i] = (char) ('A' + Math.random() * 26);
            else if (f == 1)
                ss[i] = (char) ('a' + Math.random() * 26);
            else
                ss[i] = (char) ('0' + Math.random() * 10);
            i++;
        }
        String is = new String(ss);
        return is;
    }

    /**
     * 广播
     *
     * @param context
     * @param action
     * @author TangRen
     * @time 2016-7-2
     */
    public static void Intent(Context context, String action, int postion) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("msg", postion);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    //1分=0.01元---最终得到的是分为单位
    public static int yuanToFen(String amout) {
        return (int) (Float.valueOf(amout) * 100);
    }

    //1分=0.01元---最终得到的是元为单位
    public static String fenToYuan(String amount) {
        int i = Integer.valueOf(amount);
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format((float) i / (float) 100);
    }

    public static String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dateString = format.format(date);// 当前系统时间
        return dateString;
    }


    //检查是否有网络
    public static boolean checkNetwork(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        boolean wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();
        boolean internet = manager.getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        if (wifi | internet)
            return true;
        return false;
    }

}
