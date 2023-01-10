package com.example.demo_bckj.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.TextView;

import com.example.demo_bckj.base.BasePresenter;
import com.example.demo_bckj.listener.PlayInterface;
import com.example.demo_bckj.listener.SDKListener;
import com.example.demo_bckj.manager.HttpManager;

import java.io.IOException;
import java.util.List;

/**
 * @author ZJL
 * @date 2023/1/3 10:41
 * @des
 * @updateAuthor
 * @updateDes
 */
public class HomePresenter extends BasePresenter {
    private SDKListener sdkListener;
    private List<String> accountLists, telLists;

    public void setLists(List<String> accountLists, List<String> telLists) {
        this.accountLists = accountLists;
        this.telLists = telLists;
    }

    public HomePresenter(SDKListener sdkListener) {
        this.sdkListener = sdkListener;
    }

    public void setListener(SDKListener listener) {
        this.sdkListener = listener;
    }

    public boolean init(Context context) throws IOException {
        return HttpManager.getInstance().init(context);
    }

    //数据上报
    public void getSdk(Context context) {
        HttpManager.getInstance().getSdk(context,this);
    }

    //获取手机验证码
    public void getPhoneLoginCode(Context context, String tel) {
        HttpManager.getInstance().getPhoneLoginCode(context,tel,this);
    }

    //手机号登录
    public void getPhoneLogin(Context context, String tel, String code, AlertDialog dialog) {
        HttpManager.getInstance().getPhoneLogin(context,tel,code,dialog);
    }

    //账号密码注册
    public void getLoginPwRe(Context context, String number, String pass, String pass2, AlertDialog alertDialog) {
        HttpManager.getInstance().getLoginPwRe(context,number,pass,pass2,alertDialog);
    }

    //账号密码登录
    public void getLoginPwLo(Context context, String name, String password, AlertDialog dialog) {
        HttpManager.getInstance().getLoginPwLo(context,name,password,dialog);
    }

    //快速试玩
    public void getDemoAccount(Context context, PlayInterface listener) {
        HttpManager.getInstance().getDemoAccount(context,listener);
    }


    //忘记密码
    public void forgetPwd(Context context, String number, TextView popupTvCode) {
        HttpManager.getInstance().forgetPwd(context,number,popupTvCode);
    }

    //重置密码
    public void resetPwd(Context context, String tel, String code, String password, String passwordConfirmation,
                         AlertDialog resetDialog, AlertDialog forgetDialog, AlertDialog dialog) {
        HttpManager.getInstance().resetPwd(context,tel,code,password,passwordConfirmation,resetDialog,forgetDialog,dialog);
    }


    //刷新token
    public boolean refreshToken(Context context) throws IOException {
        return HttpManager.getInstance().refreshToken(context);
    }
}
