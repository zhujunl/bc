package com.example.demo_bckj.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.example.demo_bckj.R;

import androidx.annotation.NonNull;

/**
 * @author ZJL
 * @date 2022/12/15 13:54
 * @des
 * @updateAuthor
 * @updateDes
 */
public class UserAgreeDialog extends Dialog {

    private TextView txt;
    private Button sure;

    public UserAgreeDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_agreetext);
        txt=findViewById(R.id.userAgree);
        sure=findViewById(R.id.popup_submit);

        sure.setOnClickListener(v->dismiss());
    }
}
