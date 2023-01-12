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

import com.example.demo_bckj.db.entity.AccountEntity;
import com.example.demo_bckj.listener.ClickListener;
import com.example.demo_bckj.manager.DBManager;
import com.example.demo_bckj.model.utility.FileUtil;

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
        AccountEntity account = DBManager.getInstance(context).getAccount();
        if (RoundView.isNearLeft) {
            LayoutInflater.from(context).inflate(FileUtil.getResIdFromFileName(context, "layout", "pop_left"), this);
        } else {
            LayoutInflater.from(context).inflate(FileUtil.getResIdFromFileName(context, "layout", "pop_right"), this);
        }
        iv_content = (ImageView) findViewById(FileUtil.getResIdFromFileName(context, "id", "iv_content"));
        CService = (Button) findViewById(FileUtil.getResIdFromFileName(context, "id", "iv_kefu"));
        PersonalBtn = (Button) findViewById(FileUtil.getResIdFromFileName(context, "id", "iv_account"));
        userAccount=(TextView)findViewById(FileUtil.getResIdFromFileName(context,"id","userAccount"));
        String nickName = account.getNickName();
        String tel = account.getTel();
        this.account = TextUtils.isEmpty(tel)?nickName:tel;
        String s = this.account.substring(0,3)+"****"+ this.account.substring(7, this.account.length());
        userAccount.setText(s);
        accountSwitch=(TextView)findViewById(FileUtil.getResIdFromFileName(context,"id","account_switch"));
        accountSwitch.setOnClickListener(v->click.Switch());



        CService.setOnClickListener(v->click.CService(true));
        PersonalBtn.setOnClickListener(v->click.Personal(true,true));
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
