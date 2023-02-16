package com.example.demo_bckj.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo_bckj.base.BasePresenter;
import com.example.demo_bckj.db.entity.AccountEntity;
import com.example.demo_bckj.listener.LoginCallback;
import com.example.demo_bckj.listener.PlayInterface;
import com.example.demo_bckj.manager.DBManager;
import com.example.demo_bckj.manager.HttpManager;
import com.example.demo_bckj.model.utility.FileUtil;
import com.example.demo_bckj.model.utility.SPUtils;

import java.io.IOException;

/**
 * @author ZJL
 * @date 2023/1/3 10:41
 * @des
 * @updateAuthor
 * @updateDes
 */
public class HomePresenter extends BasePresenter {

    private LoginCallback loginCallback;

    public HomePresenter(LoginCallback loginCallback) {
        HttpManager.getInstance().setHomePresenter(this);
        this.loginCallback=loginCallback;
    }

    public HomePresenter() {
        HttpManager.getInstance().setHomePresenter(this);
    }


    public boolean init(Context context) throws IOException {
        return HttpManager.getInstance().init(context);
    }

    //数据上报
    public void getSdk(Context context, boolean isUpdate) {
        int id = SPUtils.getInstance(context, "bcSP").getInt("id");
        if (id == -1) {
            HttpManager.getInstance().getSdk(context, this);
        }
        if (id != -1 && isUpdate) {
            HttpManager.getInstance().getSdk(context, this, id);
        }
    }

    //获取手机验证码
    public void getPhoneLoginCode(Context context, String tel) {
        HttpManager.getInstance().getPhoneLoginCode(context, tel, this);
    }

    //手机号登录
    public void getPhoneLogin(Context context, String tel, String code) {
        HttpManager.getInstance().getPhoneLogin(context, tel, code);
    }

    //账号密码注册
    public void getLoginPwRe(Context context, String number, String pass, String pass2, AlertDialog alertDialog, Activity activity) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                getScreenView(alertDialog, activity);
            }
        });
        HttpManager.getInstance().getLoginPwRe(context, number, pass, pass2, alertDialog, thread);
    }

    //账号密码登录
    public void getLoginPwLo(Context context, String name, String password) {
        HttpManager.getInstance().getLoginPwLo(context, name, password);
    }

    //快速试玩
    public void getDemoAccount(Context context, PlayInterface listener) {
        HttpManager.getInstance().getDemoAccount(context, listener);
    }


    //忘记密码
    public void forgetPwd(Context context, String number, TextView popupTvCode) {
        HttpManager.getInstance().forgetPwd(context, number, popupTvCode);
    }

    //重置密码
    public void resetPwd(Context context, String tel, String code, String password, String passwordConfirmation) {
        HttpManager.getInstance().resetPwd(context, tel, code, password, passwordConfirmation, null, false);
    }


    //刷新token
    public boolean refreshToken(Context context) throws IOException {
        return HttpManager.getInstance().refreshToken(context);
    }

    /**
     * 截屏
     *
     * @param registerDialog
     */
    private void getScreenView(AlertDialog registerDialog, Activity activity) {
        //获取窗口管理类,获取窗口的宽度和高度
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        //创建一个Bitmap内存区
        /*
         * Config.ARGB_8888:规定每一个像素占4个字节的存储空间
         *
         */
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //获取屏幕
        View screenView = registerDialog.getWindow().getDecorView();
        //开启绘图缓存
        screenView.setDrawingCacheEnabled(true);
        //返回屏幕View的视图缓存
        bitmap = screenView.getDrawingCache();
        FileUtil.saveImg(activity, bitmap);
    }

    public void Login(Context context) {
        if (this.loginCallback != null) {
            AccountEntity account = DBManager.getInstance(context).getAccount();
            if (account != null) {
                Toast.makeText(context, "请先退出登录账号", Toast.LENGTH_SHORT).show();
                return;
            }
            SPUtils bcSP = SPUtils.getInstance(context, "bcSP");
            this.loginCallback.login(bcSP.getBoolean("isAccount"));
        }
    }

}
