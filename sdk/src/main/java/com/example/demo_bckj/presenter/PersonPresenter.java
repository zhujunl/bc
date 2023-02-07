package com.example.demo_bckj.presenter;

import android.content.Context;
import android.widget.TextView;

import com.example.demo_bckj.base.BasePresenter;
import com.example.demo_bckj.manager.HttpManager;
import com.example.demo_bckj.view.dialog.BindNewPhoneDialog;
import com.example.demo_bckj.view.dialog.ModifyPWDialog;
import com.example.demo_bckj.view.dialog.VerifyPhoneDialog;

/**
 * @author ZJL
 * @date 2022/12/14 14:45
 * @des
 * @updateAuthor
 * @updateDes
 */
public class PersonPresenter extends BasePresenter {
    //绑定手机发送验证码
    public void BindPhoneCode(Context context, String tel, TextView TCode) {
        HttpManager.getInstance().BindPhoneCode(context, tel, TCode);
    }

    //绑定手机
    public void BindPhone(Context context, String tel, String code, BindNewPhoneDialog bindNewPhoneDialog) {
        HttpManager.getInstance().BindPhone(context, tel, code, bindNewPhoneDialog, this);
    }

    //换绑手机-发送验证码到原手机
    public void modifyBind(Context context, TextView TCode) {
        HttpManager.getInstance().modifyBind(context, TCode);
    }

    //换绑手机-发送验证码到新手机
    public void modifyBindCode(Context context, String tel, TextView TCode) {
        HttpManager.getInstance().modifyBindCode(context, tel, TCode);
    }

    //换绑手机-换绑手机
    public void modifyBindPhone(Context context, String codeOld, String codeNew, String tel, VerifyPhoneDialog dialog, BindNewPhoneDialog bindNewPhoneDialog) {
        HttpManager.getInstance().modifyBindPhone(context, codeOld, codeNew, tel, dialog, bindNewPhoneDialog, this);
    }


    //修改密码
    public void modifyPwd(Context context, String passwordOld, String password, String passwordConfirmation, ModifyPWDialog modifyPWDialog) {
        HttpManager.getInstance().modifyPwd(context, passwordOld, password, passwordConfirmation, modifyPWDialog);
    }


    public void loginOut(Context context) {
        HttpManager.getInstance().loginOut(context, false, true);
    }
}
