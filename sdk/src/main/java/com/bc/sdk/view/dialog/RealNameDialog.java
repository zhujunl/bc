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
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bc.sdk.R;
import com.bc.sdk.listener.LogoutListener;
import com.bc.sdk.listener.PfRefreshCallBack;
import com.bc.sdk.manager.DBManager;
import com.bc.sdk.manager.HttpManager;
import com.bc.sdk.model.MyCallback;
import com.bc.sdk.model.RetrofitManager;
import com.bc.sdk.model.bean.AccountPwBean;
import com.bc.sdk.model.request.RealNameRequest;
import com.bc.sdk.model.utility.StrUtil;
import com.bc.sdk.presenter.PersonPresenter;
import com.bc.sdk.service.TimeService;
import com.bc.sdk.view.fragment.PersonFragment;
import com.bc.sdk.view.round.RoundView;

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
    private TextView back;
    private ImageView remove, removeCode;
    private Button submit;
    private PersonPresenter presenter;
    private PersonFragment personFragment;
    private String n, c;
    private LogoutListener mLogoutListener;
    private PfRefreshCallBack callBack;

    public RealNameDialog(@NonNull Context context, boolean isCancelable, PersonFragment personFragment, LogoutListener mLogoutListener) {
        super(context);
        setContentView(R.layout.popup_autonym);
        this.context = context;
        this.personFragment = personFragment;
        this.mLogoutListener = mLogoutListener;
        setCancelable(isCancelable);
        initView();
    }

    public RealNameDialog(@NonNull Context context, boolean isCancelable, PfRefreshCallBack callBack, LogoutListener mLogoutListener) {
        super(context,R.style.myDialog);
        setContentView(R.layout.popup_autonym);
        this.context = context;
        this.callBack = callBack;
        this.mLogoutListener = mLogoutListener;
        setCancelable(isCancelable);
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
            n = name.getText().toString().trim();
            c = code.getText().toString().trim();
            String s = StrUtil.extractChinese(n);
            if (TextUtils.isEmpty(s)) {
                Toast.makeText(context, "请输入中文", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!StrUtil.isCard(c)) {
                Toast.makeText(context, "请输入正确身份证号码", Toast.LENGTH_SHORT).show();
                return;
            }
            setRealName(context, c, n);
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

    //实名认证
    public void setRealName(Context context, String idCode, String realName) {
        RealNameRequest realNameRequest = new RealNameRequest(idCode, realName);
        RetrofitManager.getInstance(context).getApiService().setRealName(realNameRequest).enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                AccountPwBean data = JSONObject.toJavaObject(jsStr, AccountPwBean.class);
                DBManager.getInstance(context).insertAccount(data, "");
                Integer age = data.getData().getAge();
                dismiss();
                TimeService.start(context);
                if (!(age < 18)) {
                    RealNameRegisterDialog r = new RealNameRegisterDialog(context);
                    r.show();
                }
                if (personFragment!=null)
                    personFragment.onSuccess("");
                if (callBack!=null)
                    callBack.refresh();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
