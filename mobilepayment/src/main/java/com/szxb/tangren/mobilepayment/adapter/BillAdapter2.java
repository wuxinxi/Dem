package com.szxb.tangren.mobilepayment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.szxb.tangren.mobilepayment.R;
import com.szxb.tangren.mobilepayment.inteface.CustomClickListener;
import com.szxb.tangren.mobilepayment.inteface.CustomItemLongClickListener;
import com.szxb.tangren.mobilepayment.model.RecordBean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/23 0023.
 */
public class BillAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int NORMAL_ITEM = 0;
    private static final int GROUP_ITEM = 1;

    private Context mContext;
    private List<RecordBean> mDataList;
    private LayoutInflater mLayoutInflater;

    private CustomClickListener listener;

    private CustomItemLongClickListener longClickListener;


    public BillAdapter2(Context mContext, List<RecordBean> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NORMAL_ITEM) {
            //无标题
            return new NormalItemHolder(mLayoutInflater.inflate(R.layout.bill_item_view_item, parent, false));
        } else {
            return new GroupItemHolder(mLayoutInflater.inflate(R.layout.bill_item_view_list, parent, false));
        }
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
            bindNormalItem(entity, holder.out_trade_no, holder.amout, holder.payType, holder.time, holder.today);
        }
    }

    @Override
    public int getItemCount() {
        Log.d("mDataList 的长度：", mDataList.size() + "");
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

    void bindNormalItem(RecordBean entity, TextView out_trade_no, TextView amout, TextView payType, TextView time, TextView today) {

        out_trade_no.setText(entity.getOuttradeno());
        amout.setText("￥" + entity.getMoney());
        payType.setText("(" + entity.getPaytype() + ")");
        time.setText(entity.getTimeend());
        today.setText(entity.getTime());
    }

    void bindGroupItem(RecordBean entity, GroupItemHolder holder) {
        bindNormalItem(entity, holder.out_trade_no, holder.amout, holder.payType, holder.time, holder.today);
        holder.group_item_time.setText(entity.getTime());
    }



    /**
     * 无日期标题
     */
    public class NormalItemHolder extends RecyclerView.ViewHolder {
        TextView out_trade_no;
        TextView amout;
        TextView payType;
        TextView time;
        TextView today;

        public NormalItemHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.time);
            out_trade_no = (TextView) itemView.findViewById(R.id.out_trade_no);
            amout = (TextView) itemView.findViewById(R.id.amout);
            today = (TextView) itemView.findViewById(R.id.today);
            payType = (TextView) itemView.findViewById(R.id.payType);

        }
    }

    /**
     * 带日期标题
     */
    public class GroupItemHolder extends NormalItemHolder {
        TextView group_item_time;

        public GroupItemHolder(View itemView) {
            super(itemView);
            group_item_time = (TextView) itemView.findViewById(R.id.group_item_time);
        }
    }
}
