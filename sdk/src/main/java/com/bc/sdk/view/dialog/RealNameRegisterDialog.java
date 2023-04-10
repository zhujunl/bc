package com.bc.sdk.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;

import com.bc.sdk.R;

import androidx.annotation.NonNull;

/**
 * @author ZJL
 * @date 2022/12/15 13:21
 * @des 实名注册成功弹窗
 * @updateAuthor
 * @updateDes
 */
public class RealNameRegisterDialog extends Dialog {

    private Context context;
    public RealNameRegisterDialog(@NonNull Context context) {
        super(context, R.style.myDialog);
        this.context=context;
        setContentView(R.layout.popup_register_hint);
        Button submit=findViewById(R.id.popup_submit);
        submit.setOnClickListener(v->dismiss());
    }
}
