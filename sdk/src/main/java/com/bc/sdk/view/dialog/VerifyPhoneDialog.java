package com.bc.sdk.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bc.sdk.R;
import com.bc.sdk.listener.privacyListener;
import com.bc.sdk.presenter.PersonPresenter;

import androidx.annotation.NonNull;

/**
 * @author ZJL
 * @date 2022/12/15 10:33
 * @des 验证手机弹窗
 * @updateAuthor
 * @updateDes
 */
public class VerifyPhoneDialog extends Dialog {

    private TextView TCode, service;
    private ImageView back, remove, removeCode;
    private EditText phone, code;
    private Button submit;
    private Context context;
    private BindNewPhoneDialog bindDialog;
    private PersonPresenter presenter;
    private privacyListener pListener;



    public VerifyPhoneDialog(@NonNull Context context, PersonPresenter presenter) {
        super(context, R.style.myDialog);
        this.context = context;
        this.presenter=presenter;
        setContentView(R.layout.dialog_verifyphone);
        initView();
    }

    private void initView() {
        TCode = findViewById(R.id.popup_Tv_code);
        service = findViewById(R.id.popup_service);
        back = findViewById(R.id.popup_back);
        remove = findViewById(R.id.popup_remove);
        removeCode = findViewById(R.id.popup_remove_code);
        phone = findViewById(R.id.popup_login);
        code = findViewById(R.id.popup_Et_code);
        submit = findViewById(R.id.popup_submit);

        click();
    }

    private void click() {
        back.setOnClickListener(v -> dismiss());
        TCode.setOnClickListener(v -> {
            //获取短信
            presenter.modifyBind(context,phone,TCode);
        });
        submit.setOnClickListener(v -> {
            presenter.checkCode(getContext(),phone,code,this,bindDialog);
        });
        service.setOnClickListener(v -> {
            pListener.cs(this);
        });
    }

    public void setListener(privacyListener pListener){
        this.pListener=pListener;
    }
}
