package com.example.demo_bckj.model.utility;

import android.content.Context;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author ZJL
 * @date 2023/2/10 13:25
 * @des
 * @updateAuthor
 * @updateDes
 */
public class ToastUtil{
    private static Toast toast=null;


    public static void controlToastTime(int duration) {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, duration);
    }

    public static void show(Context context,String content) {
        if (toast == null) {
            toast = Toast.makeText(context,
                    content,
                    Toast.LENGTH_LONG);
        } else {
            toast.setText(content);
        }
        toast.show();
        controlToastTime(2000);
    }


}
