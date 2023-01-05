package com.example.demo_bckj.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.example.demo_bckj.R;
import com.example.demo_bckj.view.Constants;
import com.example.demo_bckj.view.round.MyWebView;

import androidx.annotation.NonNull;

/**
 * @author ZJL
 * @date 2022/12/15 13:54
 * @des 隐私政策弹窗
 * @updateAuthor
 * @updateDes
 */
public class PrivacyDialog extends Dialog {
    MyWebView myWebView;

    public PrivacyDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_peivacy);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;// 屏幕宽度（像素）
        int height = dm.heightPixels; // 屏幕高度（像素）
        float density = dm.density;//屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;//屏幕密度dpi（120 / 160 / 240）
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        layoutParams.width = width;
        layoutParams.height = height;
        this.getWindow().setAttributes(layoutParams);

        myWebView = findViewById(R.id.webView);
        myWebView.setTittle("无限游戏用户隐私政策");
        myWebView.btnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        myWebView.show(Constants.PRIVACY);
    }

}
