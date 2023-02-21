package com.example.demo_bckj.view.round;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.demo_bckj.R;
import com.example.demo_bckj.db.dao.AccountDao;
import com.example.demo_bckj.db.entity.AccountEntity;
import com.example.demo_bckj.listener.DrawGoListener;
import com.example.demo_bckj.listener.LogoutListener;
import com.example.demo_bckj.listener.PfRefreshCallBack;
import com.example.demo_bckj.listener.privacyListener;
import com.example.demo_bckj.manager.DBManager;
import com.example.demo_bckj.manager.HttpManager;
import com.example.demo_bckj.presenter.PersonPresenter;
import com.example.demo_bckj.view.Constants;
import com.example.demo_bckj.view.dialog.BindNewPhoneDialog;
import com.example.demo_bckj.view.dialog.ModifyPWDialog;
import com.example.demo_bckj.view.dialog.RealNameDialog;
import com.example.demo_bckj.view.dialog.VerifyPhoneDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.drawerlayout.widget.DrawerLayout;

/**
 * @author ZJL
 * @date 2023/2/16 9:54
 * @des
 * @updateAuthor
 * @updateDes
 */
public class MyDrawerLayout extends DrawerLayout implements PfRefreshCallBack, LogoutListener {
    final String TAG = "MyDrawerLayout";

    private androidx.drawerlayout.widget.DrawerLayout drawerLayout;
    private Button cServiceBtn, personBtn;
    private LinearLayoutCompat cServiceLin, PersonLin;

    private TextView cServiceTxt, personTxt;

    private Context context;

    private LinearLayout csModel;
    private LinearLayout pfModel,drawerLine;
    private DrawGoListener goListener;

    public MyDrawerLayout(@NonNull Context context) {
        super(context);
        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.drawer2_layout, this);
        HttpManager.getInstance().setRefreshCallback(this);
        initView();
        click();

        csInitView();
        pfInitView();
        pfClick();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
    }

    private void initView() {
        drawerLayout = findViewById(R.id.DrawerLayout);
        cServiceLin = findViewById(R.id.cservice_lin);
        PersonLin = findViewById(R.id.person_lin);
        cServiceBtn = findViewById(R.id.cservice_btn);
        personBtn = findViewById(R.id.person_btn);
        cServiceTxt = findViewById(R.id.cservice_txt);
        personTxt = findViewById(R.id.person_txt);
        csModel = findViewById(R.id.cs_model);
        pfModel = findViewById(R.id.pf_model);
        drawerLine=findViewById(R.id.drawerLine);
        pfModel.setVisibility(View.VISIBLE);
        csModel.setVisibility(View.INVISIBLE);

        personBtn.setBackgroundResource(R.mipmap.infinite_game_tabbar_me_highlight);
        personTxt.setTextColor(getResources().getColor(R.color.selected));
        cServiceBtn.setBackgroundResource(R.mipmap.infinite_game_personal_nor);
        cServiceTxt.setTextColor(getResources().getColor(R.color.nor));
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public void changeStyle(int style) {
        switch (style) {
            case 0:
                pfModel.setVisibility(View.VISIBLE);
                csModel.setVisibility(View.INVISIBLE);
                personBtn.setBackgroundResource(R.mipmap.infinite_game_tabbar_me_highlight);
                personTxt.setTextColor(getResources().getColor(R.color.selected));
                cServiceBtn.setBackgroundResource(R.mipmap.infinite_game_personal_nor);
                cServiceTxt.setTextColor(getResources().getColor(R.color.nor));
                break;
            case 1:
                pfModel.setVisibility(View.INVISIBLE);
                csModel.setVisibility(View.VISIBLE);
                personBtn.setBackgroundResource(R.mipmap.infinite_game_tabbar_me_default);
                personTxt.setTextColor(getResources().getColor(R.color.nor));
                cServiceBtn.setBackgroundResource(R.mipmap.infinite_game_personal);
                cServiceTxt.setTextColor(getResources().getColor(R.color.selected));
                break;
            default:
                break;
        }

    }

    public void setGoListener(DrawGoListener goListener) {
        this.goListener = goListener;
    }

    private void click() {
        cServiceLin.setOnClickListener(view -> {
            Log.d(TAG, "点击cServiceLin");
            changeStyle(1);
        });
        PersonLin.setOnClickListener(view -> {
            Log.d(TAG, "点击PersonLin");
            changeStyle(0);
        });
        drawerLine.setOnClickListener(v->goListener.go());
    }

    private WebView webView;

    private void csInitView() {
        webView = findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(Constants.CUSTOMER_SERVICE);
    }

    private TextView account, phone, userMore;
    private RelativeLayout modifyPw, phoneRe, realName, userAgree, privacy;
    private Button quit;
    private String tel, nickName;

    private privacyListener listener;

    private AccountDao dao;

    private PersonPresenter presenter;

    private void pfInitView() {
        account = findViewById(R.id.userAccount);
        phone = findViewById(R.id.userPhone);
        userMore = findViewById(R.id.user_more);
        modifyPw = findViewById(R.id.modifyPwRe);
        realName = findViewById(R.id.RealNameRe);
        userAgree = findViewById(R.id.userAgreeRe);
        privacy = findViewById(R.id.PrivacyRe);
        quit = findViewById(R.id.user_quit_login);
        phoneRe = findViewById(R.id.phoneRe);
    }

    private void pfInitData() {
        if (presenter == null) {
            presenter = new PersonPresenter();
        }
        dao = DBManager.getInstance(context).getDao();
        AccountEntity query = dao.query();
        nickName = query.getNickName();
        tel = query.getTel();
        if (TextUtils.isEmpty(tel)) {
            account.setText(nickName);
            phone.setText("绑定");
            phone.setTextColor(0xFF5293FF);
        } else {
            String s = tel.substring(0, 3) + "****" + tel.substring(7, tel.length());
            account.setText(s);
            phone.setText(s);
        }
        boolean isAuthenticated = query.getAuthenticated();
        if (isAuthenticated) {
            userMore.setText("已认证");
            userMore.setTextColor(0xFF999999);
            realName.setClickable(false);
        } else {
            userMore.setText("未认证");
            userMore.setTextColor(0xFF5293FF);
            realName.setClickable(true);
        }
    }


    private void pfClick() {
        Log.d(TAG, "realName.isClickable();==" + realName.isClickable());
        phoneRe.setOnClickListener(v -> {
            if (TextUtils.isEmpty(tel)) {
                //绑定手机号
                BindNewPhoneDialog bindNewPhoneDialog = new BindNewPhoneDialog(context, presenter);
                bindNewPhoneDialog.show();
            } else {
                //验证手机号
                VerifyPhoneDialog verifyPhoneDialog = new VerifyPhoneDialog(context, presenter);
                verifyPhoneDialog.setListener(listener);
                verifyPhoneDialog.show();
            }
        });
        modifyPw.setOnClickListener(v -> {
            ModifyPWDialog modifyPWDialog = new ModifyPWDialog(context, presenter);
            modifyPWDialog.show();
        });
        if (realName.isClickable()) {
            realName.setOnClickListener(v -> {
                //实名认证
                RealNameDialog realNameDialog = new RealNameDialog(context, true, this, null);
                realNameDialog.show();

            });
        }
        userAgree.setOnClickListener(v -> {
            listener.user();
        });
        privacy.setOnClickListener(v -> {
            listener.privacy();
        });
        quit.setOnClickListener(v -> {
            presenter.loginOut(context);
        });
    }

    @Override
    public void refresh() {
        pfInitData();
    }


    @Override
    public void out(boolean isLoginShow) {

    }

    public void setListener(privacyListener listener) {
        this.listener = listener;
    }
}
