package com.bc.sdk.view.round;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bc.sdk.listener.ClickListener;
import com.bc.sdk.manager.DBManager;
import com.bc.sdk.model.utility.FileUtil;
import com.bc.sdk.db.entity.AccountEntity;

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
        String s = !TextUtils.isEmpty(tel)?tel.substring(0,3)+"****"+ tel.substring(7, tel.length()):nickName;
        userAccount.setText(s);
        accountSwitch=(TextView)findViewById(FileUtil.getResIdFromFileName(context,"id","account_switch"));
        accountSwitch.setOnClickListener(v->click.Switch());



        CService.setOnClickListener(v-> {
            RoundView.getInstance().showSmallwin(context,click,10);
            click.CService(true);
        });
        PersonalBtn.setOnClickListener(v-> {
            RoundView.getInstance().showSmallwin(context,click,10);
            click.Personal(true, true);
        });
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
