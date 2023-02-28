package com.bc.sdk.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bc.sdk.R;
import com.bc.sdk.model.utility.DeviceIdUtil;
import com.bc.sdk.presenter.PersonPresenter;
import com.bc.sdk.view.Constants;

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
    private VerifyPhoneDialog dialog;
    private PersonPresenter presenter;
    private String codeOld;

    public BindNewPhoneDialog(@NonNull Context context, VerifyPhoneDialog dialog, PersonPresenter presenter) {
        super(context);
        this.context=context;
        this.dialog=dialog;
        this.presenter=presenter;
        setContentView(R.layout.dialog_bindnewphone);
        initView();
    }

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
            if (Constants.isFastDoubleClick(getContext())){
                return;
            }
            if (TextUtils.isEmpty(code.getText().toString().trim())){
                Toast.makeText(context, "请输入验证码", Toast.LENGTH_SHORT).show();
                return;
            }
            if (dialog==null){
                presenter.BindPhone(context,phone.getText().toString().trim(),code.getText().toString().trim(),this);
            }else {
                presenter.modifyBindPhone(context,codeOld,code.getText().toString().trim(),phone.getText().toString().trim(),dialog,this);
            }
        });
        TCode.setOnClickListener(v->{
            String p = phone.getText().toString().trim();
            boolean mobileNO = DeviceIdUtil.isMobileNO(p);
            if (!mobileNO) {
                Toast.makeText(getContext(), "请输入合法手机号", Toast.LENGTH_SHORT).show();
            } else {
                //获取短信
                if (dialog==null) {
                    presenter.BindPhoneCode(context, p, TCode);
                }else {
                    presenter.modifyBindCode(context,p,TCode);
                }
            }


        });
    }

    public void set(String trim1) {
        codeOld=trim1;
    }

}
