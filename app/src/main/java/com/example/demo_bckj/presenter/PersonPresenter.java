package com.example.demo_bckj.presenter;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.demo_bckj.base.BasePresenter;
import com.example.demo_bckj.listener.SDKListener;
import com.example.demo_bckj.model.MyCallback;
import com.example.demo_bckj.model.RetrofitManager;
import com.example.demo_bckj.model.bean.AccountPwBean;
import com.example.demo_bckj.model.utility.CountDownTimerUtils;
import com.example.demo_bckj.model.utility.SPUtils;
import com.example.demo_bckj.view.dialog.BindNewPhoneDialog;
import com.example.demo_bckj.view.dialog.ModifyPWDialog;
import com.example.demo_bckj.view.dialog.RealNameDialog;
import com.example.demo_bckj.view.dialog.RealNameRegisterDialog;
import com.example.demo_bckj.view.dialog.UnderAgeDialog;
import com.example.demo_bckj.view.dialog.VerifyPhoneDialog;

import okhttp3.ResponseBody;

/**
 * @author ZJL
 * @date 2022/12/14 14:45
 * @des
 * @updateAuthor
 * @updateDes
 */
public class PersonPresenter extends BasePresenter {
    private SDKListener sdkListener;

    public PersonPresenter(SDKListener sdkListener) {
        this.sdkListener = sdkListener;
    }

    //绑定手机发送验证码
    public void BindPhoneCode(Context context, String tel, TextView TCode) {
        RetrofitManager.getInstance(context).getApiService().BindPhoneCode(tel).enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                Toast.makeText(context, "发送成功", Toast.LENGTH_SHORT).show();
                CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(TCode, 60000, 1000);
                countDownTimerUtils.start();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //绑定手机
    public void BindPhone(Context context, String tel, String code, BindNewPhoneDialog bindNewPhoneDialog) {
        RetrofitManager.getInstance(context).getApiService().BindPhone(tel, code).enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                view.onSuccess("");
                bindNewPhoneDialog.dismiss();
                SPUtils.getInstance(context, "bcSP").put("tel", tel);
                Toast.makeText(context, "绑定成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //换绑手机-发送验证码到原手机
    public void modifyBind(Context context, TextView TCode) {
        RetrofitManager.getInstance(context).getApiService().modifyBind().enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                Toast.makeText(context, "发送成功", Toast.LENGTH_SHORT).show();
                CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(TCode, 60000, 1000);
                countDownTimerUtils.start();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //换绑手机-发送验证码到新手机
    public void modifyBindCode(Context context, String tel, TextView TCode) {
        RetrofitManager.getInstance(context).getApiService().modifyBindCode(tel).enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                Toast.makeText(context, "发送成功", Toast.LENGTH_SHORT).show();
                CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(TCode, 60000, 1000);
                countDownTimerUtils.start();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //换绑手机-换绑手机
    public void modifyBindPhone(Context context, String codeOld, String codeNew, String tel, VerifyPhoneDialog dialog, BindNewPhoneDialog bindNewPhoneDialog) {
        RetrofitManager.getInstance(context).getApiService().modifyBindPhone(codeOld, codeNew, tel).enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                view.onSuccess("");
                dialog.dismiss();
                bindNewPhoneDialog.dismiss();
                AccountPwBean data = JSONObject.toJavaObject(jsStr, AccountPwBean.class);
                SPUtils.getInstance(context, "bcSP").save(data, "");
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //是否已完成实名认证
    public void IsRealName(Context context) {
        RetrofitManager.getInstance(context).getApiService().IsRealName().enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                view.onSuccess("");
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //实名认证
    public void setRealName(Context context, String idCode, String realname, RealNameDialog realNameDialog) {
        RetrofitManager.getInstance(context).getApiService().setRealName(idCode, realname).enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                AccountPwBean data = JSONObject.toJavaObject(jsStr, AccountPwBean.class);
                SPUtils.getInstance(context, "bcSP").save(data, "");
                Integer age = data.getData().getAge();
                view.onSuccess("");
                realNameDialog.dismiss();
                if (age < 18) {
                    //未成年弹窗
                    UnderAgeDialog underAgeDialog = new UnderAgeDialog(context);
                    underAgeDialog.show();
                } else {
                    RealNameRegisterDialog r = new RealNameRegisterDialog(context);
                    r.show();
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //用户在线
    public void isOnline(Context context) {
        RetrofitManager.getInstance(context).getApiService().isOnline().enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {

            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //修改密码
    public void modifyPwd(Context context, String passwordOld, String password, String passwordConfirmation, ModifyPWDialog modifyPWDialog) {
        RetrofitManager.getInstance(context).getApiService().modifyPwd(passwordOld, password, passwordConfirmation).enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                modifyPWDialog.dismiss();
                Toast.makeText(context, "密码修改成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void loginOut(Context context) {
        RetrofitManager.getInstance(context).getApiService().logout().enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                sdkListener.SignOut();
                SPUtils.getInstance(context, "bcSP").clear();
                System.exit(0);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
