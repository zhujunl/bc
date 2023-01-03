package com.example.demo_bckj.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;

import com.example.demo_bckj.R;
import com.example.demo_bckj.listener.RechargeListener;
import com.example.demo_bckj.model.bean.RechargeOrder;

import androidx.annotation.NonNull;

/**
 * @author ZJL
 * @date 2022/12/16 11:02
 * @des 未成年人充值提醒弹窗
 * @updateAuthor
 * @updateDes
 */
public class RechargeDialog extends Dialog {

    private Context context;

    public RechargeDialog(@NonNull Context context, RechargeOrder rechargeOrder, RechargeListener listener) {
        super(context);
        setContentView(R.layout.dialog_recharge);
        this.context=context;

        Button btn=findViewById(R.id.popup_submit);
        btn.setOnClickListener(v->{
            dismiss();
            RechargeSubDialog rechargeSubDialog=new RechargeSubDialog(context,rechargeOrder,listener);
            rechargeSubDialog.show();
        });
    }
}
