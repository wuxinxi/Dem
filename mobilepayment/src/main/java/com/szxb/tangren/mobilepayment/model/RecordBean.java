package com.szxb.tangren.mobilepayment.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Administrator on 2016/9/23 0023.
 */
@Table(name = "RecordBean", id = "_id")
public class RecordBean extends Model {
    @Column(name = "outtradeno")
    public String outtradeno;

    @Column(name = "transactionid")
    public String transactionid;

    @Column(name = "timeend")
    public String timeend;

    @Column(name = "money")
    public String money;

    @Column(name = "paytype")
    public String paytype;

    @Column(name = "time")
    public String time;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }


    public String getOuttradeno() {
        return outtradeno;
    }

    public void setOuttradeno(String outtradeno) {
        this.outtradeno = outtradeno;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public String getTimeend() {
        return timeend;
    }

    public void setTimeend(String timeend) {
        this.timeend = timeend;
    }
}
