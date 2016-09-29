package com.szxb.tangren.mobilepayment.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.szxb.tangren.mobilepayment.R;
import com.szxb.tangren.mobilepayment.inteface.CustomClickListener;
import com.szxb.tangren.mobilepayment.inteface.CustomItemLongClickListener;

/**
 * Created by Administrator on 2016/9/23 0023.
 */
public class GroupItemHolder extends NormalItemHolder {

    public TextView group_item_time;

    public GroupItemHolder(Context context, View itemView, CustomClickListener listener, CustomItemLongClickListener longClickListener) {
        super(context, itemView, listener, longClickListener);
        group_item_time = (TextView) itemView.findViewById(R.id.group_item_time);
    }
}
