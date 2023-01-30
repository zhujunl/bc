package com.example.demo_bckj.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import com.example.demo_bckj.R;
import com.example.demo_bckj.manager.HttpManager;

import androidx.annotation.NonNull;

/**
 * @author ZJL
 * @date 2022/12/15 13:23
 * @des 防沉迷提示弹窗
 * @updateAuthor
 * @updateDes
 */
public class UnderAgeDialog extends Dialog {
    private String TAG = "UnderAgeDialog";

    private Context context;
    private Button btn;

    public UnderAgeDialog(@NonNull Context context, String t) {
        super(context);
        setContentView(R.layout.dialog_underage);
        this.context = context;
        btn = findViewById(R.id.btn_under_age);
        TextView txt = findViewById(R.id.txt);
        txt.setText(t);
        btn.setClickable(false);
        setCancelable(false);
        Count c = new Count(context, btn, 10000, 1000);
        c.start();
    }

    class Count extends CountDownTimer {
        private Context context;
        private Button btn;

        public Count(Context context, Button btn, long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            this.context = context;
            this.btn = btn;
        }

        @Override
        public void onTick(long l) {
            btn.setText(l / 1000 + "s后自动退出游戏");  //设置倒计时时间
        }

        @Override
        public void onFinish() {
            HttpManager.getInstance().loginOut(context,true);
        }
    }
}
