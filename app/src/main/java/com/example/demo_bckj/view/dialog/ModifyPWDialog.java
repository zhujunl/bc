package com.example.demo_bckj.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.demo_bckj.R;
import com.example.demo_bckj.presenter.PersonPresenter;

import androidx.annotation.NonNull;

/**
 * @author ZJL
 * @date 2022/12/15 11:40
 * @des
 * @updateAuthor
 * @updateDes
 */
public class ModifyPWDialog extends Dialog {

    private String TAG = "ModifyPWDialog";
    private Context context;
    private ImageView back, remove, AMove;
    private EditText pw, newPw, newPwA;
    private Button submit;
    private PersonPresenter presenter;

    public ModifyPWDialog(@NonNull Context context, PersonPresenter presenter) {
        super(context);
        this.context = context;
        this.presenter=presenter;
        setContentView(R.layout.dialog_modifypw);
        initView();

    }

    private void initView() {
        submit = findViewById(R.id.popup_submit);
        back = findViewById(R.id.popup_back);
        remove = findViewById(R.id.popup_remove_code);
        AMove = findViewById(R.id.pwAgain_remove);
        pw = findViewById(R.id.popup_login);
        newPw = findViewById(R.id.popup_Et_code);
        newPwA = findViewById(R.id.pwAgain);

        click();
    }

    private void click() {
        back.setOnClickListener(v->{
            dismiss();
        });
        submit.setOnClickListener(v->{
            String trim = pw.getText().toString().trim();
            String trim1 = newPw.getText().toString().trim();
            String trim2 = newPwA.getText().toString().trim();
            if (!TextUtils.equals(trim1,trim2)){
                Toast.makeText(context, "两次密码不一致", Toast.LENGTH_SHORT).show();
            }else {
                presenter.modifyPwd(context,trim,trim1,trim2,this);
            }
        });
    }
}
