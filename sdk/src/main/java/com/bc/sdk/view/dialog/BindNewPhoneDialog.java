package com.bc.sdk.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bc.sdk.R;
import com.bc.sdk.presenter.PersonPresenter;

import androidx.annotation.NonNull;

/**
 * @author ZJL
 * @date 2022/12/15 11:00
 * @des 绑定新手机弹窗
 * @updateAuthor
 * @updateDes
 */
public class BindNewPhoneDialog extends Dialog {
    private Context context;
    private TextView TCode,tittle;
    private ImageView back,remove,removeCode;
    private EditText phone,code;
    private Button submit;
    private VerifyPhoneDialog dialog;//dialog不为空时执行修改绑定手机
    private PersonPresenter presenter;
    private String codeOld;

    //绑定新手机，用于修改绑定手机
    public BindNewPhoneDialog(@NonNull Context context, VerifyPhoneDialog dialog, PersonPresenter presenter) {
        super(context);
        this.context=context;
        this.dialog=dialog;
        this.presenter=presenter;
        setContentView(R.layout.dialog_bindnewphone);
        initView();
    }

    //绑定手机，用于初次绑定
    public BindNewPhoneDialog(@NonNull Context context,PersonPresenter presenter) {
        super(context);
        this.context=context;
        this.presenter=presenter;
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
            presenter.submitBindPhone(context,phone,code,codeOld,dialog,this);
        });
        TCode.setOnClickListener(v->{
            presenter.getPhoneCode(context,phone,TCode,dialog);


        });
    }

    public void set(String trim1) {
        codeOld=trim1;
    }

}
