package com.szxb.tangren.mobilepayment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.szxb.tangren.mobilepayment.R;
import com.szxb.tangren.mobilepayment.model.DemoBean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/23 0023.
 */
public class DemoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int NORMAL_ITEM = 0;
    private static final int GROUP_ITEM = 1;

    private Context mContext;
    private List<DemoBean> mDataList;
    private LayoutInflater mLayoutInflater;

    public DemoAdapter(Context mContext, List<DemoBean> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    /**
     * 渲染具体的ViewHolder
     *
     * @param viewGroup ViewHolder的容器
     * @param i         一个标志，我们根据该标志可以实现渲染不同类型的ViewHolder
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        if (i == NORMAL_ITEM) {
            return new NormalItemHolder(mLayoutInflater.inflate(R.layout.fragment_base_swipe_list, viewGroup, false));
        } else {
            return new GroupItemHolder(mLayoutInflater.inflate(R.layout.fragment_base_swipe_group_item, viewGroup, false));
        }
    }

    /**
     * 绑定ViewHolder的数据。
     *
     * @param viewHolder
     * @param i          数据源list的下标
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        DemoBean entity = mDataList.get(i);

        if (null == entity)
            return;

        if (viewHolder instanceof GroupItemHolder) {
            bindGroupItem(entity, (GroupItemHolder) viewHolder);
        } else {
            NormalItemHolder holder = (NormalItemHolder) viewHolder;
            bindNormalItem(entity, holder.newsTitle, holder.newsIcon);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 决定元素的布局使用哪种类型
     *
     * @param position 数据源List的下标
     * @return 一个int型标志，传递给onCreateViewHolder的第二个参数
     */
    @Override
    public int getItemViewType(int position) {
        //第一个要显示时间
        if (position == 0)
            return GROUP_ITEM;

        String currentDate = mDataList.get(position).getData();
        int prevIndex = position - 1;
        boolean isDifferent = !mDataList.get(prevIndex).getData().equals(currentDate);
        return isDifferent ? GROUP_ITEM : NORMAL_ITEM;
    }

    @Override
    public long getItemId(int position) {
        return mDataList.get(position).getId();
    }

    void bindNormalItem(DemoBean entity, TextView newsTitle, ImageView newsIcon) {
        if (entity.getData().isEmpty()) {

            if (newsIcon.getVisibility() != View.GONE)
                newsIcon.setVisibility(View.GONE);
        } else {
//            ZImage.getInstance().load(entity.getIconUrl(), newsIcon,
//                    ConfigConstant.LIST_ITEM_IMAGE_SIZE_DP, ConfigConstant.LIST_ITEM_IMAGE_SIZE_DP, true, mContext.getMyApplication().canRequestImage());

            if (newsIcon.getVisibility() != View.VISIBLE)
                newsIcon.setVisibility(View.VISIBLE);
        }
        newsTitle.setText(Html.fromHtml(entity.getTitle()));
    }

    void bindGroupItem(DemoBean entity, GroupItemHolder holder) {
        bindNormalItem(entity, holder.newsTitle, holder.newsIcon);
        holder.newsTime.setText(entity.getData());
    }

    void showNewsDetail(int pos) {
        DemoBean entity = mDataList.get(pos);
//        NewsDetailActivity.actionStart(mContext, entity.getNewsID(), entity.getRecommendAmount(), entity.getCommentAmount());
    }

    /**
     * 标题
     */
    public class NormalItemHolder extends RecyclerView.ViewHolder {
        TextView newsTitle;
        ImageView newsIcon;

        public NormalItemHolder(View itemView) {
            super(itemView);
            newsTitle = (TextView) itemView.findViewById(R.id.base_swipe_item_title);
            newsIcon = (ImageView) itemView.findViewById(R.id.base_swipe_item_icon);
            itemView.findViewById(R.id.base_swipe_item_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    showNewsDetail(getPosition());
                }
            });
        }
    }

    /**
     * 带日期标题
     */
    public class GroupItemHolder extends NormalItemHolder {
        TextView newsTime;

        public GroupItemHolder(View itemView) {
            super(itemView);
            newsTime = (TextView) itemView.findViewById(R.id.base_swipe_group_item_time);
        }
    }
}
