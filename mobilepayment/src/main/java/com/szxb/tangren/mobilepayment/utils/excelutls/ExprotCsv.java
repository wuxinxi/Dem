package com.szxb.tangren.mobilepayment.utils.excelutls;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.szxb.tangren.mobilepayment.db.OrderDBManager;
import com.szxb.tangren.mobilepayment.model.RecordBean;
import com.szxb.tangren.mobilepayment.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by Administrator on 2016/9/27 0027.
 */
public class ExprotCsv {

    private String title[] = {"订单号", "系统订单号", "交易时间", "交易金额", "支付方式", "系统时间"};

    private OrderDBManager manager;

    private Context context;

    public ExprotCsv(Context context) {
        manager = new OrderDBManager();
        this.context = context;
    }

    public void saveCsv() {
        List<RecordBean> list = manager.query();
        StringBuffer buffer = new StringBuffer();
        buffer.append("订单号,系统订单号,交易时间,交易金额,支付方式,系统时间\r\n");
        for (RecordBean bean : list) {
            buffer.append(bean.getOuttradeno() + "," + bean.getTransactionid()
                    + "," + bean.getTimeend() + "," + bean.getMoney() + ","
                    + bean.getPaytype() + "," + bean.getTime() + "\r\n");
        }

        try {

            String data = buffer.toString();
            String filename = "交易账单_" + Utils.getDate() + ".csv";

            String path = Environment.getExternalStorageDirectory() + "/Bills";
            if (!new File(path).exists()) {
                new File(path).mkdirs();
            }
            File file = new File(path, filename);
            OutputStream out = new FileOutputStream(file);


            // excel需要BOM签名才能解析utf-8格式的编码
            byte b[] = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
            out.write(b);
            out.write(data.getBytes());

            out.close();
            Toast.makeText(context, "文件导出成功！请到SD卡中查看", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("异常", e.toString());
        }

    }


}
