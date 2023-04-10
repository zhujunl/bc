package com.bc.sdk.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bc.sdk.R;

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

    public RechargeDialog(@NonNull Context context, String text) {
        super(context, R.style.myDialog);
        setContentView(R.layout.dialog_recharge);
        this.context = context;
        Button btn = findViewById(R.id.popup_submit);
        TextView textView = findViewById(R.id.privacyTxt);
        textView.setText(text);
        btn.setOnClickListener(v -> {
            dismiss();
        });
    }

    public RechargeDialog(@NonNull Context context, String tittle, String tip, String txt) {
        super(context, R.style.myDialog);
        setContentView(R.layout.dialog_recharge);
        this.context = context;
        Button btn = findViewById(R.id.popup_submit);
        TextView textView = findViewById(R.id.privacyTxt);
        TextView rechargeTittle = findViewById(R.id.rechargeTittle);
        SpannableString tx = new SpannableString(txt);
        tx.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(txt);
                intent.setData(uri);
                context.startActivity(intent);
            }
        }, 0, tx.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(tip + "\n");
        textView.append(tx);
        rechargeTittle.setText(tittle);
        btn.setOnClickListener(v -> {
            dismiss();
        });
    }
}
