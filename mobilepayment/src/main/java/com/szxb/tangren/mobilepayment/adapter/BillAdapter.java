package com.szxb.tangren.mobilepayment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szxb.tangren.mobilepayment.R;
import com.szxb.tangren.mobilepayment.inteface.CustomClickListener;
import com.szxb.tangren.mobilepayment.inteface.CustomItemLongClickListener;
import com.szxb.tangren.mobilepayment.model.RecordBean;
import com.szxb.tangren.mobilepayment.utils.DateUtils;
import com.szxb.tangren.mobilepayment.viewholder.GroupItemHolder;
import com.szxb.tangren.mobilepayment.viewholder.NormalItemHolder;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Administrator on 2016/9/23 0023.
 */
public class BillAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int NORMAL_ITEM = 0;
    private static final int GROUP_ITEM = 1;

    private Context mContext;
    private List<RecordBean> mDataList;
    private LayoutInflater mLayoutInflater;

    private CustomClickListener listener;

    private CustomItemLongClickListener longClickListener;

    private LinearLayout linearLayout;


    public BillAdapter(Context mContext, List<RecordBean> mDataList, LinearLayout linearLayout) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        this.linearLayout = linearLayout;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == NORMAL_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_item_view_item, parent, false);
            NormalItemHolder holder = new NormalItemHolder(mContext, itemView, listener, longClickListener);
            return holder;
        } else if (viewType == GROUP_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_item_view_list, parent, false);
            GroupItemHolder holder = new GroupItemHolder(mContext, itemView, listener, longClickListener);
            return holder;
        }
        return null;//
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        RecordBean entity = mDataList.get(position);

        if (null == entity)
            return;

        if (viewHolder instanceof GroupItemHolder) {
            bindGroupItem(entity, (GroupItemHolder) viewHolder);
        } else {
            NormalItemHolder holder = (NormalItemHolder) viewHolder;
            bindNormalItem(entity, holder.out_trade_no, holder.amout, holder.payType, holder.time, holder.today, holder.imageType);
        }
    }

    @Override
    public int getItemCount() {
        Log.d("mDataList 的长度：", mDataList.size() + "");
        if (mDataList.size() == 0)
            linearLayout.setBackgroundResource(R.mipmap.null_record);
        else
            linearLayout.setBackgroundResource(0);
        return mDataList.size();
    }

    @Override
    public long getItemId(int position) {
        return mDataList.get(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        //第一个要显示时间
        if (position == 0)
            return GROUP_ITEM;

        String currentDate = mDataList.get(position).getTime();
        int prevIndex = position - 1;
        boolean isDifferent = !mDataList.get(prevIndex).getTime().equals(currentDate);
        return isDifferent ? GROUP_ITEM : NORMAL_ITEM;
    }

    void bindNormalItem(RecordBean entity, TextView out_trade_no, TextView amout, TextView payType, TextView time, TextView today, ImageView imageType) {

        out_trade_no.setText(entity.getOuttradeno());
        amout.setText("￥" + entity.getMoney());
        payType.setText("(" + entity.getPaytype() + ")");
        time.setText(entity.getTimeend());

        try {
            if (DateUtils.IsToday(entity.getTime()))
                today.setText("今天");
            else if (DateUtils.IsYesterday(entity.getTime()))
                today.setText("昨天");
            else
                today.setText(entity.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (entity.getPaytype().equals("微信支付")) {
            imageType.setBackgroundResource(R.mipmap.pay_icon_weixin);
        } else if (entity.getPaytype().equals("支付宝支付")) {
            imageType.setBackgroundResource(R.mipmap.pay6_normal);
        } else if (entity.getPaytype().equals("QQ支付")) {
            imageType.setBackgroundResource(R.mipmap.pay_qq_default);
        } else {
            imageType.setBackgroundResource(R.mipmap.pay_shuffle);
        }
    }

    void bindGroupItem(RecordBean entity, GroupItemHolder holder) {
        bindNormalItem(entity, holder.out_trade_no, holder.amout, holder.payType, holder.time, holder.today, holder.imageType);
        holder.group_item_time.setText(entity.getTime());
    }

    public void setClickListener(CustomClickListener clickListener) {
        this.listener = clickListener;
    }

    public void setLongClickListener(CustomItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }


}
