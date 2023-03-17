package com.bc.sdk.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bc.sdk.R;
import com.bc.sdk.listener.LogoutListener;
import com.bc.sdk.listener.PfRefreshCallBack;
import com.bc.sdk.manager.HttpManager;
import com.bc.sdk.presenter.PersonPresenter;
import com.bc.sdk.view.round.RoundView;

import androidx.annotation.NonNull;

/**
 * @author ZJL
 * @date 2022/12/15 13:10
 * @des 实名认证弹窗
 * @updateAuthor
 * @updateDes
 */
public class RealNameDialog extends Dialog {

    private Context context;
    private EditText name, code;
    private TextView back;
    private ImageView remove, removeCode;
    private Button submit;
    private PersonPresenter presenter;
    private LogoutListener mLogoutListener;
    private PfRefreshCallBack callBack;

    public RealNameDialog(@NonNull Context context, boolean isCancelable, PfRefreshCallBack callBack, LogoutListener mLogoutListener) {
        super(context,R.style.myDialog);
        setContentView(R.layout.popup_autonym);
        this.context = context;
        this.callBack = callBack;
        this.mLogoutListener = mLogoutListener;
        setCancelable(isCancelable);
        presenter=new PersonPresenter();
        initView();
    }

    private void initView() {
        name = findViewById(R.id.popup_login);
        code = findViewById(R.id.popup_Et_code);
        remove = findViewById(R.id.popup_remove);
        removeCode = findViewById(R.id.popup_remove_code);
        submit = findViewById(R.id.popup_submit);
        back = findViewById(R.id.back);
        back.setVisibility(mLogoutListener == null ? View.INVISIBLE : View.VISIBLE);
        click();
    }

    private void click() {
        submit.setOnClickListener(v -> {
            presenter.submitRealName(context,name,code,this,callBack);
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                remove.setVisibility(TextUtils.isEmpty(name.getText().toString()) ? View.INVISIBLE : View.VISIBLE);
            }
        });
        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                removeCode.setVisibility(TextUtils.isEmpty(code.getText().toString()) ? View.INVISIBLE : View.VISIBLE);
            }
        });
        remove.setOnClickListener(v -> {
            name.setText("");
        });
        removeCode.setOnClickListener(v -> {
            code.setText("");
        });
        back.setOnClickListener(v -> {
            dismiss();
            RoundView.getInstance().closeRoundView(getContext());
            HttpManager.getInstance().loginOut(context, false, true);
            if (mLogoutListener != null) {
                mLogoutListener.out(true);
            }
        });
    }
}
