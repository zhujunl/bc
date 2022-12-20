package com.example.demo_bckj.view.round;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.demo_bckj.inter.ClickListener;
import com.example.demo_bckj.model.utility.FileUtil;
import com.example.demo_bckj.model.utility.SPUtils;

/**
 * @author WangKun
 * @description: 悬浮窗展开状态
 * @date :2022/9/15
 */
public class RoundWindowBigView extends LinearLayout {

    private Context context;
    private ImageView iv_content;
    private Button CService,PersonalBtn;
    private ClickListener click;
    private TextView userAccount,accountSwitch;
    private String account="13665883504";


    public RoundWindowBigView(Context context,ClickListener click) {
        super(context);
        this.context = context;
        this.click=click;
        SPUtils sp = SPUtils.getInstance(context, "bcSP");
        if (RoundView.isNearLeft) {
            LayoutInflater.from(context).inflate(FileUtil.getResIdFromFileName(context, "layout", "pop_left"), this);
            userAccount=(TextView)findViewById(FileUtil.getResIdFromFileName(context,"id","userAccount"));
            String nickName = sp.getString("nick_name", "");
            String tel = sp.getString("tel", "");
            account= TextUtils.isEmpty(tel)?nickName:tel;
            String s = account.substring(0,3)+"****"+account.substring(7,account.length());
            userAccount.setText(s);
            accountSwitch=(TextView)findViewById(FileUtil.getResIdFromFileName(context,"id","account_switch"));
            accountSwitch.setOnClickListener(v->click.Switch());
        } else {
            LayoutInflater.from(context).inflate(FileUtil.getResIdFromFileName(context, "layout", "pop_right"), this);
        }
        iv_content = (ImageView) findViewById(FileUtil.getResIdFromFileName(context, "id", "iv_content"));
        CService = (Button) findViewById(FileUtil.getResIdFromFileName(context, "id", "iv_kefu"));
        PersonalBtn = (Button) findViewById(FileUtil.getResIdFromFileName(context, "id", "iv_account"));




        CService.setOnClickListener(v->click.CService(true));
        PersonalBtn.setOnClickListener(v->click.Personal(true));
        setupViews();
    }
    //设置视图
    private void setupViews() {
        iv_content.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        // 只能先创建在移除，不然有问题
                        RoundView.getInstance().createSmallWindow(context,click);
                        RoundView.getInstance().removeBigWindow(context);
                    }
                });
            }
        });

    }

    public void setVisibilityState(int state){
        this.setVisibility(state);
    }

}
