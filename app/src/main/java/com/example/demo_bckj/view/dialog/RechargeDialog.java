package com.example.demo_bckj.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;

import com.example.demo_bckj.R;

import androidx.annotation.NonNull;

/**
 * @author ZJL
 * @date 2022/12/16 11:02
 * @des
 * @updateAuthor
 * @updateDes
 */
public class RechargeDialog extends Dialog {

    private Context context;

    public RechargeDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_recharge);
        this.context=context;

        Button btn=findViewById(R.id.popup_submit);
        btn.setOnClickListener(v->{
            dismiss();
            RechargeSubDialog rechargeSubDialog=new RechargeSubDialog(context);
            rechargeSubDialog.show();
        });
    }
}
