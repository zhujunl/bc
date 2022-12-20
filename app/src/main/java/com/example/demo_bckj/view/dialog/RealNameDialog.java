package com.example.demo_bckj.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.demo_bckj.R;
import com.example.demo_bckj.model.utility.StrUtil;
import com.example.demo_bckj.presenter.PersonPresenter;

import androidx.annotation.NonNull;

/**
 * @author ZJL
 * @date 2022/12/15 13:10
 * @des
 * @updateAuthor
 * @updateDes
 */
public class RealNameDialog extends Dialog {

    private Context context;
    private EditText name, code;
    private ImageView remove, removeCode;
    private Button submit;
    private PersonPresenter presenter;
    private String n, c;

    public RealNameDialog(@NonNull Context context, PersonPresenter presenter) {
        super(context);
        setContentView(R.layout.popup_autonym);
        this.context = context;
        this.presenter = presenter;
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
            presenter.setRealName(context,c,n,this);
        });
        remove.setOnClickListener(v -> {
            name.setText("");
        });
        removeCode.setOnClickListener(v -> {
            code.setText("");
        });
    }


}
