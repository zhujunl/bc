package com.example.demo_bckj.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.demo_bckj.R;
import com.example.demo_bckj.model.MyCallback;
import com.example.demo_bckj.model.RetrofitManager;
import com.example.demo_bckj.model.bean.AccountPwBean;
import com.example.demo_bckj.model.utility.SPUtils;
import com.example.demo_bckj.model.utility.StrUtil;
import com.example.demo_bckj.presenter.PersonPresenter;
import com.example.demo_bckj.service.TimeService;
import com.example.demo_bckj.view.fragment.PersonFragment;

import androidx.annotation.NonNull;
import okhttp3.ResponseBody;

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
    private ImageView remove, removeCode;
    private Button submit;
    private PersonPresenter presenter;
    private PersonFragment personFragment;
    private String n, c;

    public RealNameDialog(@NonNull Context context, boolean isCancelable,PersonFragment personFragment) {
        super(context);
        setContentView(R.layout.popup_autonym);
        this.context = context;
        this.personFragment=personFragment;
        setCancelable(isCancelable);
        initView();
    }

    private void initView() {
        name = findViewById(R.id.popup_login);
        code = findViewById(R.id.popup_Et_code);
        remove = findViewById(R.id.popup_remove);
        removeCode = findViewById(R.id.popup_remove_code);
        submit = findViewById(R.id.popup_submit);
        click();
    }

    private void click() {
        submit.setOnClickListener(v -> {
            n = name.getText().toString().trim();
            c = code.getText().toString().trim();
            String s = StrUtil.extractChinese(n);
            if (TextUtils.isEmpty(s)){
                Toast.makeText(context,"请输入中文",Toast.LENGTH_SHORT).show();
                return;
            }
            if (!StrUtil.isCard(c)){
                Toast.makeText(context,"请输入正确身份证号码",Toast.LENGTH_SHORT).show();
                return;
            }
            setRealName(context,c,n);
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
              remove.setVisibility(TextUtils.isEmpty(name.getText().toString())? View.INVISIBLE:View.VISIBLE);
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
                removeCode.setVisibility(TextUtils.isEmpty(code.getText().toString())? View.INVISIBLE:View.VISIBLE);
            }
        });
        remove.setOnClickListener(v -> {
            name.setText("");
        });
        removeCode.setOnClickListener(v -> {
            code.setText("");
        });
    }

    //实名认证
    public void setRealName(Context context, String idCode, String realName) {
        RetrofitManager.getInstance(context).getApiService().setRealName(idCode, realName).enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                AccountPwBean data = JSONObject.toJavaObject(jsStr, AccountPwBean.class);
                SPUtils.getInstance(context, "bcSP").save(data, "");
                Integer age = data.getData().getAge();
                dismiss();
                if (age < 18) {
                    //未成年弹窗
                    TimeService.start(context);
                } else {
                    RealNameRegisterDialog r = new RealNameRegisterDialog(context);
                    r.show();
                }
                personFragment.onSuccess("");
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
