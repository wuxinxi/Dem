package com.szxb.tangren.mobilepayment.utils.popuwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.szxb.tangren.mobilepayment.R;

/**
 * Created by Administrator on 2016/9/21 0021.
 */
public class SelectPicPopupWindow extends PopupWindow {
    private LinearLayout wechat_type, ali_type, ten_type;
    private TextView cancel;
    private Context context;
    private View view;

    public SelectPicPopupWindow(Context context, View.OnClickListener onclickListener) {
        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popupwindow_view2, null);
        wechat_type = (LinearLayout) view.findViewById(R.id.wechat_pay);
        ali_type = (LinearLayout) view.findViewById(R.id.ali_pay);
        ten_type = (LinearLayout) view.findViewById(R.id.ten_pay);
        cancel = (TextView) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ten_type.setOnClickListener(onclickListener);
        ali_type.setOnClickListener(onclickListener);
        wechat_type.setOnClickListener(onclickListener);
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });


    }

}
