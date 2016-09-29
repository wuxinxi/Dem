package com.szxb.tangren.mobilepayment.db;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.szxb.tangren.mobilepayment.model.RecordBean;
import com.yolanda.nohttp.Logger;

import java.util.List;

/**
 * Created by Administrator on 2016/9/23 0023.
 */
public class OrderDBManager {

    /**
     * 查询交易记录
     *
     * @return
     */
    public static List<RecordBean> query() {
        return new Select().from(RecordBean.class)
                .orderBy("_id desc")
                .execute();
    }

    /**
     * 保存交易信息
     *
     * @param dingdanhao    订单号
     * @param timeend       交易完成时间
     * @param wfdiangdanhao 威富通订单号
     * @param money         支付金额
     * @param paytype       支付类型
     */
    public static void addRecord(String dingdanhao, String timeend, String wfdiangdanhao, String money, String paytype, String today) {
        RecordBean bean = new RecordBean();
        bean.outtradeno = dingdanhao;
        bean.timeend = timeend;
        bean.transactionid = wfdiangdanhao;
        bean.paytype = paytype;
        bean.money = money;
        bean.time = today;
        bean.save();
    }

    /**
     * 当退款成功，则删除交易记录
     *
     * @param out_trade_no
     */
    public static void delete(String out_trade_no) {
        new Delete().from(RecordBean.class).where("outtradeno=?", out_trade_no).execute();
    }

    //根据日期查询订单
    public static List<RecordBean> queryNo(String date) {
        return new Select().from(RecordBean.class)
                .orderBy("_id desc")
                .where("time=?", date)
                .execute();
    }

    //根据支付方式查询订单
    public static List<RecordBean> queryPaytype(String paytype) {
        return new Select().from(RecordBean.class)
                .orderBy("_id desc")
                .where("paytype=?", paytype)
                .execute();
    }

    //根据支付类型与时间范围
    public static List<RecordBean> queryTypeAndtime(String payType, String startTime, String endTime) {
        return new Select().from(RecordBean.class)
                .orderBy("_id desc")
                .where("paytype=?", payType)
                .and("time between '" + startTime + "' and '" + endTime + "'")
                .execute();
    }

    //根据支付类型与时间范围
    public static List<RecordBean> startTimeToendTime(String startTime, String endTime) {
        return new Select().from(RecordBean.class)
                .orderBy("_id desc")
                .and("time between '" + startTime + "' and '" + endTime + "'")
                .execute();
    }

    //根据支付类型与时间
    public static List<RecordBean> queryTypeAndtime(String payType, String time) {
        return new Select().from(RecordBean.class)
                .orderBy("_id desc")
                .where("paytype=?,time=?", payType, time)
                .execute();
    }

    public static List<RecordBean> getMoney(String date) {
        return new Select().from(RecordBean.class)
                .where("time=?", date)
                .execute();
    }


    //查询昨天的记录
    public static List<RecordBean> getYesterdayReocrd(String type, String yesterday) {
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE,0);
//        String yesterday = new SimpleDateFormat("yyyy-MM-dd ").format(cal.getTime());
        Logger.d("yesterday" + yesterday);
        return new Select()
                .from(RecordBean.class)
                .orderBy("_id desc")
                .where("time=?", yesterday)
                .execute();
    }

    //查询本周记录
    public static List<RecordBean> getWeekReocrd(String type) {
        return new Select()
                .from(RecordBean.class)
                .orderBy("_id desc")
                .where("time between datetime(date(datetime('now',strftime('-%w day','now'))),'+1 second')\n" +
                        " \n" +
                        "and datetime(date(datetime('now',(6 - strftime('%w day','now'))||' day','1 day')),'-1 second')")
                .execute();
    }

    //查询上周记录 X
    public static List<RecordBean> getLastWeekReocrd(String type) {
        return new Select()
                .from(RecordBean.class)
                .orderBy("_id desc")
                .where("time between datetime(date(datetime('now',strftime('-%w day','now'))),'-1 second')\n" +
                        " \n" +
                        "and datetime(date(datetime('now',(6 + strftime('%w day','now'))||' day','1 day')),'+1 second')")
                .execute();
    }

    //查询本月记录
    public static List<RecordBean> getMonthReocrd(String type) {
        return new Select()
                .from(RecordBean.class)
                .orderBy("_id desc")
                .where("time between datetime('now','start of month','+1 second') and     \n" +
                        " \n" +
                        "datetime('now','start of month','+1 month','-1 second')  ")
                .execute();
    }

    //查询上个月记录
    public static List<RecordBean> getLastMonthReocrd(String type) {
        return new Select()
                .from(RecordBean.class)
                .orderBy("_id desc")
                .where("time>=datetime('now','start of month','-1 month','-0 day') and time <datetime('now','start of month','+0 month','-1 day')")
                .execute();
    }


}