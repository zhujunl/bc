package com.example.demo_bckj.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.demo_bckj.base.BasePresenter;
import com.example.demo_bckj.inter.ClickListener;
import com.example.demo_bckj.inter.PlayInterface;
import com.example.demo_bckj.model.MyCallback;
import com.example.demo_bckj.model.RetrofitManager;
import com.example.demo_bckj.model.bean.AccountPwBean;
import com.example.demo_bckj.model.bean.DateUpBean;
import com.example.demo_bckj.model.bean.PlayBean;
import com.example.demo_bckj.model.utility.CountDownTimerUtils;
import com.example.demo_bckj.model.utility.FileUtil;
import com.example.demo_bckj.model.utility.SPUtils;
import com.example.demo_bckj.view.round.RoundView;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * @author WangKun
 * @description:
 * @date :2022/9/23 15:16
 */
public class DemoPresenter extends BasePresenter {
    //数据上报
    public void getSdk(Context context){
            RetrofitManager.getInstance(context)
                    .getApiService()
                    .getSdk()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ResponseBody value) {
                            if (view!=null){
                                view.onSuccess(value);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if(view!=null){
                                view.onError(e.getMessage());
                            }
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
    }
    //获取手机验证码
    public void getPhoneLoginCode(Context context,String tel){
            RetrofitManager.getInstance(context)
                    .getApiService()
                    .getPhoneLoginCode(tel)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ResponseBody value) {
                            JSONObject jsStr = FileUtil.getResponseBody(value);
                            DateUpBean object = JSONObject.toJavaObject(jsStr,DateUpBean.class);
                            if (view!=null){
                                if (object.getCode()==200){
                                    view.onSuccess(object);
                                }else {
                                    view.onError(object.getMessage());
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if(view!=null){
                                view.onError(e.getMessage());
                            }
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
    }
    //手机号登录
    public void getPhoneLogin(Context context, ClickListener listener, String tel, String code, AlertDialog dialog){
            RetrofitManager.getInstance(context)
                    .getApiService()
                    .getPhoneLogin(tel,code).enqueue(new MyCallback<ResponseBody>() {
                        @Override
                        public void onSuccess(JSONObject jsStr) {
                            AccountPwBean data = JSONObject.toJavaObject(jsStr, AccountPwBean.class);
                            SPUtils.getInstance(context, "bcSP").save(data,"");
                            dialog.dismiss();
                            RoundView.getInstance().showRoundView(context, listener);
                            listener.Personal(false);
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    });
    }
    //账号密码注册
    public void getLoginPwRe(Context context, String number, String pass, String pass2, AlertDialog alertDialog,ClickListener listener){
        RetrofitManager.getInstance(context)
                    .getApiService()
                    .getLoginPwRe(number,pass,pass2).enqueue(new MyCallback<ResponseBody>() {
                    @Override
                    public void onSuccess(JSONObject jsStr) {
                        AccountPwBean data = JSONObject.toJavaObject(jsStr,AccountPwBean.class);
                        SPUtils.getInstance(context, "bcSP").save(data,pass);
                        alertDialog.dismiss();
                        RoundView.getInstance().showRoundView(context,listener);
                        listener.Personal(false);
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    //账号密码登录
    public void getLoginPwLo(Context context, String name, String password, AlertDialog dialog, ClickListener listener){
        RetrofitManager.getInstance(context)
                .getApiService()
                .getLoginPwLo(name,password).enqueue(new MyCallback<ResponseBody>() {
                    @Override
                    public void onSuccess(JSONObject jsStr) {
                        if (dialog!=null)
                            dialog.dismiss();
                        AccountPwBean data = JSONObject.toJavaObject(jsStr, AccountPwBean.class);
                        SPUtils.getInstance(context, "bcSP").save(data,password);
                        RoundView.getInstance().showRoundView(context, listener);
                        listener.Personal(false);
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //快速试玩
    public void getDemoAccount(Context context, PlayInterface listener){
            RetrofitManager.getInstance(context)
                    .getApiService()
                    .getDemoAccount()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ResponseBody value) {
                            JSONObject jsStr = FileUtil.getResponseBody(value);
                            PlayBean response = JSONObject.toJavaObject(jsStr,PlayBean.class);
                            if (response.getCode()==0){
                                listener.onSuccess(response.getData().getAccount(),response.getData().getPassword());
                            }else {
                                listener.onError(response.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {

                }
            });


    }


    //忘记密码
    public void forgetPwd(Context context, String number, TextView popupTvCode) {
        RetrofitManager.getInstance(context).getApiService().forgetPwd(number).enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                Toast.makeText(context, "发送成功", Toast.LENGTH_SHORT).show();
                CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(popupTvCode, 60000, 1000);
                countDownTimerUtils.start();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //重置密码
    public void resetPwd(Context context, String tel, String code, String password, String passwordConfirmation, AlertDialog resetDialog, AlertDialog forgetDialog) {
        RetrofitManager.getInstance(context).getApiService().resetPwd(tel, code, password, passwordConfirmation).enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                Toast.makeText(context, "密码修改成功", Toast.LENGTH_SHORT).show();
                resetDialog.dismiss();
                forgetDialog.dismiss();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
