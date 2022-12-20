package com.example.demo_bckj.view.round;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.demo_bckj.inter.ClickListener;
import com.example.demo_bckj.model.utility.FileUtil;

/**
 * @author WangKun
 * @description: 悬浮窗贴边状态
 * @date :2022/9/15
 */
public class RoundWindowHideView extends LinearLayout {

    private ImageView hideview;
    private Context context;
    private ClickListener clck;

    public RoundWindowHideView(Context context,ClickListener click) {
        super(context);
        this.context = context;
        if (RoundView.isNearLeft) {
            LayoutInflater.from(context).inflate(FileUtil.getResIdFromFileName(context, "layout", "layout_hide_float_left"), this);
        } else {
            LayoutInflater.from(context).inflate(FileUtil.getResIdFromFileName(context, "layout", "layout_hide_float_right"), this);
        }
        hideview = (ImageView) findViewById(FileUtil.getResIdFromFileName(context, "id", "hide_float_iv"));


        setupViews();
    }
    private void setupViews() {
        hideview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 只能先创建在移除，不然有问题
                RoundView.getInstance().createSmallWindow(context,clck);
                RoundView.getInstance().removeHideWindow(context);
            }
        });

    }

    public void setVisibilityState(int state){
        this.setVisibility(state);
    }

}
