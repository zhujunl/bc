package com.example.demo_bckj.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;

import com.example.demo_bckj.R;

import androidx.annotation.NonNull;

/**
 * @author ZJL
 * @date 2022/12/15 13:21
 * @des
 * @updateAuthor
 * @updateDes
 */
public class RealNameRegisterDialog extends Dialog {

    private Context context;

    public RealNameRegisterDialog(@NonNull Context context) {
        super(context);
        this.context=context;
        setContentView(R.layout.popup_register_hint);
        Button submit=findViewById(R.id.popup_submit);
        submit.setOnClickListener(v->dismiss());
    }
}
