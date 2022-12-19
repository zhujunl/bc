package com.example.demo_bckj.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.demo_bckj.R;

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
    private EditText name,code;
    private ImageView remove,removeCode;
    private Button submit;

    public RealNameDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.popup_autonym);
        this.context=context;
        initView();
    }

    private void initView(){
        name=findViewById(R.id.popup_login);
        code=findViewById(R.id.popup_login);
        remove=findViewById(R.id.popup_remove);
        removeCode=findViewById(R.id.popup_remove_code);
        submit=findViewById(R.id.popup_submit);

        click();
    }

    private void click(){
        submit.setOnClickListener(v->{
            ////未成年弹窗
//            UnderAgeDialog underAgeDialog=new UnderAgeDialog(getContext());
//            underAgeDialog.show();
            RealNameRegisterDialog r=new RealNameRegisterDialog(getContext());
            r.show();
            dismiss();
        });
        remove.setOnClickListener(v->{

        });
        removeCode.setOnClickListener(v->{

        });
    }


}
