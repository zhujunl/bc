package com.example.demo_bckj.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo_bckj.R;
import com.example.demo_bckj.model.utility.DeviceIdUtil;
import com.example.demo_bckj.presenter.PersonPresenter;
import com.example.demo_bckj.view.Constants;
import com.example.demo_bckj.view.fragment.PersonFragment;

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
    private PersonFragment.privacyListener pListener;



    public VerifyPhoneDialog(@NonNull Context context, PersonPresenter presenter) {
        super(context);
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
            boolean mobileNO = DeviceIdUtil.isMobileNO(phone.getText().toString().trim());
            if (!mobileNO) {
                Toast.makeText(getContext(), "请输入合法手机号", Toast.LENGTH_SHORT).show();
            } else {
                //获取短信
                presenter.modifyBind(context,TCode);
            }
        });
        submit.setOnClickListener(v -> {
            if (Constants.isFastDoubleClick(getContext())){
                return;
            }
            if (TextUtils.isEmpty(phone.getText().toString().trim())||TextUtils.isEmpty(code.getText().toString().trim())
            ||!DeviceIdUtil.isMobileNO(phone.getText().toString().trim())){
                Toast.makeText(getContext(), "请输入手机号与验证码", Toast.LENGTH_SHORT).show();
                return;
            }
            if (bindDialog==null){
                bindDialog = new BindNewPhoneDialog(context, this, presenter);
            }
            bindDialog.set(code.getText().toString().trim());
            hide();
            bindDialog.show();
        });
        service.setOnClickListener(v -> {
            pListener.cs(this);
        });
    }

    public void setListener(PersonFragment.privacyListener pListener){
        this.pListener=pListener;
    }
}
