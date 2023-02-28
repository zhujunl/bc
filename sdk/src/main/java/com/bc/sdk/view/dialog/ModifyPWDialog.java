package com.bc.sdk.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bc.sdk.R;
import com.bc.sdk.db.entity.AccountEntity;
import com.bc.sdk.manager.DBManager;
import com.bc.sdk.manager.HttpManager;
import com.bc.sdk.model.utility.DeviceIdUtil;
import com.bc.sdk.presenter.PersonPresenter;
import com.bc.sdk.view.Constants;

import androidx.annotation.NonNull;

/**
 * @author ZJL
 * @date 2022/12/15 11:40
 * @des 修改密码弹窗
 * @updateAuthor
 * @updateDes
 */
public class ModifyPWDialog extends Dialog {

    private String TAG = "ModifyPWDialog";
    private Context context;
    private ImageView back;
    private EditText pw, newPw, newPwA;
    private TextView forget;
    private Button submit;
    private PersonPresenter presenter;

    public ModifyPWDialog(@NonNull Context context, PersonPresenter presenter) {
        super(context);
        this.context = context;
        this.presenter = presenter;
        setContentView(R.layout.dialog_modifypw);
        initView();
    }

    private void initView() {
        submit = findViewById(R.id.popup_submit);
        back = findViewById(R.id.popup_back);
        pw = findViewById(R.id.popup_login);
        newPw = findViewById(R.id.popup_Et_code);
        newPwA = findViewById(R.id.pwAgain);
        forget = findViewById(R.id.forget);
        click();
    }

    private void click() {
        back.setOnClickListener(v -> {
            dismiss();
        });
        submit.setOnClickListener(v -> {
            if (Constants.isFastDoubleClick(getContext())){
                return;
            }
            String trim = pw.getText().toString().trim();
            String trim1 = newPw.getText().toString().trim();
            String trim2 = newPwA.getText().toString().trim();
            if (!TextUtils.equals(trim1, trim2)) {
                Toast.makeText(context, "两次密码不一致", Toast.LENGTH_SHORT).show();
            } else {
                presenter.modifyPwd(context, trim, trim1, trim2, this);
            }
        });
        forget.setOnClickListener(v -> {
            AccountEntity query = DBManager.getInstance(context).getDao().query();
            String tel = query.getTel();
            if (TextUtils.isEmpty(tel)){
                Toast.makeText(context, "该账号未绑定手机，请绑定手机！", Toast.LENGTH_SHORT).show();
                return;
            }
            popupForgetPassword();
        });
    }

    //验证手机号
    private void popupForgetPassword() {
        View inflate = LayoutInflater.from(context).inflate(R.layout.popup_code_phone, null);
        EditText popupLogin = inflate.findViewById(R.id.popup_login);
        EditText popupEtCode = inflate.findViewById(R.id.popup_Et_code);
        TextView popupTvCode = inflate.findViewById(R.id.popup_Tv_code);
        ImageView popup_remove = inflate.findViewById(R.id.popup_remove);
        ImageView popup_back = inflate.findViewById(R.id.popup_back);
        ImageView popup_remove_code = inflate.findViewById(R.id.popup_remove_code);
        Button popupSubmit = inflate.findViewById(R.id.popup_submit);
        TextView popup_loginPw = inflate.findViewById(R.id.popup_loginPw);
        RelativeLayout verifyCode = inflate.findViewById(R.id.verifyCode);
        verifyCode.setVisibility(View.INVISIBLE);
        setContentView(inflate);
        //验证
        popupSubmit.setOnClickListener(view -> {
            String trim1 = popupLogin.getText().toString().trim();
            String trim2 = popupEtCode.getText().toString().trim();
            if (TextUtils.isEmpty(trim1) || TextUtils.isEmpty(trim2)) {
                Toast.makeText(context, "请输入手机号与验证码", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!DeviceIdUtil.isMobileNO(trim1)) {
                Toast.makeText(context, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            popupResetPassword(trim1, trim2);
        });
        //返回上一级
        popup_back.setOnClickListener(view -> {
            setContentView(R.layout.dialog_modifypw);
            initView();
        });

        //输入框监听
        popupLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (popupLogin.getText().toString() != null && !popupLogin.getText().toString().equals("")) {
                    popup_remove.setVisibility(View.VISIBLE);
                    popup_remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupLogin.setText("");
                        }
                    });

                } else {
                    popup_remove.setVisibility(View.INVISIBLE);
                }
            }
        });
        //验证码输入监听
        popupEtCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (popupEtCode.getText().toString() != null && !popupEtCode.getText().toString().equals("")) {
                    popup_remove_code.setVisibility(View.VISIBLE);
                    popup_remove_code.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupEtCode.setText("");
                        }
                    });

                } else {
                    popup_remove_code.setVisibility(View.INVISIBLE);
                }
            }
        });
        popup_loginPw.setVisibility(View.INVISIBLE);
        popupTvCode.setOnClickListener(view -> {
            String trim1 = popupLogin.getText().toString().trim();
            if (TextUtils.isEmpty(trim1)) {
                Toast.makeText(context, "请输入手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!DeviceIdUtil.isMobileNO(trim1)) {
                Toast.makeText(context, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            HttpManager.getInstance().forgetPwd(context, trim1, popupTvCode);
        });
    }

    //重置密码
    private void popupResetPassword(String n, String c) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.popup_reset_password, null);
        ImageView popup_back = inflate.findViewById(R.id.popup_back);
        EditText popup_new_password = inflate.findViewById(R.id.popup_new_password);
        EditText popup_password_pw = inflate.findViewById(R.id.popup_password_pw);
        ImageView popup_remove = inflate.findViewById(R.id.popup_remove);
        ImageView popup_remove_pw_pw = inflate.findViewById(R.id.popup_remove_pw_pw);
        Button popupSubmit = inflate.findViewById(R.id.popup_submit);
        TextView popup_loginPw = inflate.findViewById(R.id.popup_loginPw);
        setContentView(inflate);

        //确认重置密码
        popupSubmit.setOnClickListener(view -> {
            String trim1 = popup_new_password.getText().toString().trim();
            String trim2 = popup_password_pw.getText().toString().trim();
            HttpManager.getInstance().resetPwd(context, n, c, trim1, trim2, this, true);
        });
        //返回上一级
        popup_back.setOnClickListener(view -> {
            popupForgetPassword();
        });
        //输入框监听
        popup_new_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (popup_new_password.getText().toString() != null && !popup_new_password.getText().toString().equals("")) {
                    popup_remove.setVisibility(View.VISIBLE);
                    popup_remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popup_new_password.setText("");
                        }
                    });

                } else {
                    popup_remove.setVisibility(View.INVISIBLE);
                }
            }
        });

        popup_password_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (popup_password_pw.getText().toString() != null && !popup_password_pw.getText().toString().equals("")) {
                    popup_remove_pw_pw.setVisibility(View.VISIBLE);
                    popup_remove_pw_pw.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popup_password_pw.setText("");
                        }
                    });

                } else {
                    popup_remove_pw_pw.setVisibility(View.INVISIBLE);
                }
            }
        });
        popup_loginPw.setVisibility(View.INVISIBLE);
    }
}
