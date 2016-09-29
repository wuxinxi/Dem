package com.szxb.tangren.mobilepayment.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.szxb.tangren.mobilepayment.R;


/**
 * Created by Administrator on 2016/8/30 0030.
 */
public class WaitDialog extends Dialog {

    private Context context;
    private CharSequence msg;
    private LayoutInflater inflater;

    public WaitDialog(Context context, CharSequence msg) {
        super(context, R.style.Custom_Progress);
        this.context = context;
        this.msg = msg;
        this.inflater = inflater.from(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        View view = inflater.inflate(R.layout.progress_custom, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.spinnerImageView);
        AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
        // 开始动画
        spinner.start();
        TextView message = (TextView) view.findViewById(R.id.message);
        if (msg != null && msg.length() > 0) {
            message.setVisibility(View.VISIBLE);
            message.setText(msg);
        } else
            message.setVisibility(View.GONE);
        setContentView(view);
        getWindow().getAttributes().gravity = Gravity.CENTER;

    }

}
