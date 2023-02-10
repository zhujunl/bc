package com.example.demo_bckj.view.round;

import android.content.Context;
import android.view.View;

/**
 * @author ZJL
 * @date 2023/2/9 16:42
 * @des
 * @updateAuthor
 * @updateDes
 */
public class MyButton extends androidx.appcompat.widget.AppCompatButton {


    private OnClickListener click;

    public MyButton(Context context) {
        super(context);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

}
