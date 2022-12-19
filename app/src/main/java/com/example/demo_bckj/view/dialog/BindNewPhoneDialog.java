package com.example.demo_bckj.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo_bckj.R;

import androidx.annotation.NonNull;

/**
 * @author ZJL
 * @date 2022/12/15 11:00
 * @des
 * @updateAuthor
 * @updateDes
 */
public class BindNewPhoneDialog extends Dialog {
    private Context context;
    private TextView TCode,tittle;
    private ImageView back,remove,removeCode;
    private EditText phone,code;
    private Button submit;
    private VerifyPhoneDialog dialog;

    public BindNewPhoneDialog(@NonNull Context context,VerifyPhoneDialog dialog) {
        super(context);
        this.context=context;
        this.dialog=dialog;
        setContentView(R.layout.dialog_bindnewphone);
        initView();
    }

    public BindNewPhoneDialog(@NonNull Context context) {
        super(context);
        this.context=context;
        setContentView(R.layout.dialog_bindnewphone);
        initView();
    }

    private void initView(){
        submit=findViewById(R.id.popup_submit);
        back=findViewById(R.id.popup_back);
        remove=findViewById(R.id.popup_remove);
        removeCode=findViewById(R.id.popup_remove_code);
        phone=findViewById(R.id.popup_login);
        code=findViewById(R.id.popup_Et_code);
        TCode=findViewById(R.id.popup_Tv_code);
        tittle=findViewById(R.id.bindTittle);

        if (dialog==null){
            tittle.setText("绑定手机号");
            submit.setText("绑定");
        }

        click();
    }

    private void click(){
        back.setOnClickListener(v->{
            dismiss();
            if (dialog!=null){
                dialog.show();
            }
        });
        submit.setOnClickListener(v->{
            if (dialog!=null){
                dialog.dismiss();
            }
            dismiss();
            Toast.makeText(context, dialog!=null?"换绑成功":"绑定成功", Toast.LENGTH_SHORT).show();
        });
    }
}
