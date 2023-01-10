package com.example.demo_bckj.manager;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.demo_bckj.base.BasePresenter;
import com.example.demo_bckj.listener.ClickListener;
import com.example.demo_bckj.listener.IBaseView;
import com.example.demo_bckj.listener.PlayInterface;
import com.example.demo_bckj.listener.SDKListener;
import com.example.demo_bckj.model.MyCallback;
import com.example.demo_bckj.model.RetrofitManager;
import com.example.demo_bckj.model.bean.AccountPwBean;
import com.example.demo_bckj.model.bean.DateUpBean;
import com.example.demo_bckj.model.bean.OnlineBean;
import com.example.demo_bckj.model.bean.PlayBean;
import com.example.demo_bckj.model.bean.URLBean;
import com.example.demo_bckj.model.utility.CountDownTimerUtils;
import com.example.demo_bckj.model.utility.FileUtil;
import com.example.demo_bckj.model.utility.SPUtils;
import com.example.demo_bckj.view.Constants;
import com.example.demo_bckj.view.dialog.BindNewPhoneDialog;
import com.example.demo_bckj.view.dialog.ModifyPWDialog;
import com.example.demo_bckj.view.dialog.RealNameDialog;
import com.example.demo_bckj.view.dialog.UnderAgeDialog;
import com.example.demo_bckj.view.dialog.VerifyPhoneDialog;
import com.example.demo_bckj.view.round.RoundView;

import java.io.IOException;
import java.util.List;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * @author ZJL
 * @date 2023/1/9 16:00
 * @des
 * @updateAuthor
 * @updateDes
 */
public class HttpManager {
    private String TAG = "HttpManager";
    private SDKListener sdkListener;
    private ClickListener listener;
    private List<String> accountLists, telLists;

    private static HttpManager instance;

    public static HttpManager getInstance() {
        if (instance == null) {
            instance = new HttpManager();
        }
        return instance;
    }

    public HttpManager() {
    }

    public void setListener(SDKListener listener,ClickListener clickListener) {
        this.sdkListener = listener;
        this.listener=clickListener;
    }

    public void setLists(List<String> accountLists, List<String> telLists) {
        this.accountLists = accountLists;
        this.telLists = telLists;
    }

    public boolean init(Context context) throws IOException {
        Response<ResponseBody> execute = RetrofitManager.getInstance(context).getApiService().init().execute();
        ResponseBody body = execute.body();
        JSONObject json = FileUtil.getResponseBody(body);
        Object code = json.get("code");
        if (code.toString().equals("0")) {
            Object data = json.get("data");
            JSONObject jsonObject = JSONObject.parseObject(data.toString());
            URLBean urlBean = JSONObject.toJavaObject(jsonObject, URLBean.class);
            Constants.DEVICE = urlBean.getProtocol_url().getDevice();
            Constants.REGISTER = urlBean.getProtocol_url().getRegister();
            Constants.PRIVACY = urlBean.getProtocol_url().getPrivacy();
            return true;
        } else {
            return false;
        }
    }

    //数据上报
    public void getSdk(Context context, BasePresenter presenter) {
        RetrofitManager.getInstance(context)
                .getApiService()
                .getSdk()
                .enqueue(new MyCallback<ResponseBody>() {
                    @Override
                    public void onSuccess(JSONObject jsStr) {
                        IBaseView view = presenter.getView();
                        if (view != null) {
                            view.onSuccess(jsStr);
                        }
                    }

                    @Override
                    public void onError(String message) {
                        IBaseView view = presenter.getView();
                        if (view != null) {
                            view.onError(message);
                        }
                    }
                });

    }

    //获取手机验证码
    public void getPhoneLoginCode(Context context, String tel, BasePresenter presenter) {
        RetrofitManager.getInstance(context)
                .getApiService()
                .getPhoneLoginCode(tel)
                .enqueue(new MyCallback<ResponseBody>() {
                    @Override
                    public void onSuccess(JSONObject jsStr) {
                        DateUpBean object = JSONObject.toJavaObject(jsStr, DateUpBean.class);
                        IBaseView view = presenter.getView();
                        if (view != null) {
                            if (object.getCode() == 200) {
                                view.onSuccess(object);
                            } else {
                                view.onError(object.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onError(String message) {
                        IBaseView view = presenter.getView();
                        if (view != null) {
                            view.onError(message);
                        }
                    }
                });

    }

    //手机号登录
    public void getPhoneLogin(Context context, String tel, String code, AlertDialog dialog) {
        RetrofitManager.getInstance(context)
                .getApiService()
                .getPhoneLogin(tel, code).enqueue(new MyCallback<ResponseBody>(context) {
                    @Override
                    public void onSuccess(JSONObject jsStr) {
                        isOnline(context);
                        AccountPwBean data = JSONObject.toJavaObject(jsStr, AccountPwBean.class);
                        SPUtils.getInstance(context, "bcSP").save(data, "");
                        sdkListener.Login(data.getData());
                        dialog.dismiss();
                        if (!telLists.contains(tel)) {
                            telLists.add(tel);
                            SPUtils.getInstance(context, "open").put("tel", telLists);
                        }
                        RoundView.getInstance().showRoundView(context, listener);
                        listener.Personal(false, data.getData().getAuthenticated());
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //账号密码注册
    public void getLoginPwRe(Context context, String number, String pass, String pass2, AlertDialog alertDialog) {
        RetrofitManager.getInstance(context)
                .getApiService()
                .getLoginPwRe(number, pass, pass2).enqueue(new MyCallback<ResponseBody>(context) {
                    @Override
                    public void onSuccess(JSONObject jsStr) {
                        isOnline(context);
                        AccountPwBean data = JSONObject.toJavaObject(jsStr, AccountPwBean.class);
                        SPUtils.getInstance(context, "bcSP").save(data, pass);
                        sdkListener.Login(data.getData());
                        alertDialog.dismiss();
                        if (!accountLists.contains(number)) {
                            accountLists.add(number);
                            SPUtils.getInstance(context, "open").put("account", accountLists);
                        }
                        RoundView.getInstance().showRoundView(context, listener);
                        listener.Personal(false, data.getData().getAuthenticated());
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //账号密码登录
    public void getLoginPwLo(Context context, String name, String password, AlertDialog dialog) {
        RetrofitManager.getInstance(context)
                .getApiService()
                .getLoginPwLo(name, password).enqueue(new MyCallback<ResponseBody>(context) {
                    @Override
                    public void onSuccess(JSONObject jsStr) {
                        if (dialog != null)
                            dialog.dismiss();
                        isOnline(context);
                        AccountPwBean data = JSONObject.toJavaObject(jsStr, AccountPwBean.class);
                        SPUtils.getInstance(context, "bcSP").save(data, password);
                        sdkListener.Login(data.getData());
                        if (!accountLists.contains(name)) {
                            accountLists.add(name);
                            SPUtils.getInstance(context, "open").put("account", accountLists);
                        }
                        RoundView.getInstance().showRoundView(context, listener);
                        listener.Personal(false, data.getData().getAuthenticated());
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //快速试玩
    public void getDemoAccount(Context context, PlayInterface listener) {
        RetrofitManager.getInstance(context)
                .getApiService()
                .getDemoAccount()
                .enqueue(new MyCallback<ResponseBody>() {
                    @Override
                    public void onSuccess(JSONObject jsStr) {
                        PlayBean response = JSONObject.toJavaObject(jsStr, PlayBean.class);
                        if (response.getCode() == 0) {
                            listener.onSuccess(response.getData().getAccount(), response.getData().getPassword());
                        } else {
                            listener.onError(response.getMessage());
                        }
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
    public void resetPwd(Context context, String tel, String code, String password, String passwordConfirmation, AlertDialog resetDialog, AlertDialog forgetDialog, AlertDialog dialog) {
        RetrofitManager.getInstance(context).getApiService().resetPwd(tel, code, password, passwordConfirmation).enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                Object message = jsStr.get("message");
                Toast.makeText(context, message.toString(), Toast.LENGTH_SHORT).show();
                resetDialog.dismiss();
                forgetDialog.dismiss();
                dialog.show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }


    //刷新token
    public boolean refreshToken(Context context) throws IOException {
        Response<ResponseBody> execute = RetrofitManager.getInstance(context).getApiService().refreshToken().execute();
        if (execute.code() == 200) {
            Headers headers = execute.headers();
            String Authorization = headers.get("Authorization");
            SPUtils.getInstance(context, "bcSP").put("Authorization", Authorization);
            JSONObject json = FileUtil.getResponseBody(execute.body());
            Object code = json.get("code");
            if (code.toString().equals("0")) {
                isOnline(context);
                AccountPwBean data = JSONObject.toJavaObject(json, AccountPwBean.class);
                SPUtils.getInstance(context, "bcSP").save(data, "");
                sdkListener.Login(data.getData());
                RoundView.getInstance().showRoundView(context, listener);
                return true;
            }
        }
        return false;
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
    public void BindPhone(Context context, String tel, String code, BindNewPhoneDialog bindNewPhoneDialog,BasePresenter presenter) {
        RetrofitManager.getInstance(context).getApiService().BindPhone(tel, code).enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                bindNewPhoneDialog.dismiss();
                SPUtils.getInstance(context, "bcSP").put("tel", tel);
                Toast.makeText(context, "绑定成功", Toast.LENGTH_SHORT).show();
                presenter.getView().onSuccess("");
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
    public void modifyBindPhone(Context context, String codeOld, String codeNew, String tel, VerifyPhoneDialog dialog,
                                BindNewPhoneDialog bindNewPhoneDialog,BasePresenter presenter) {
        RetrofitManager.getInstance(context).getApiService().modifyBindPhone(codeOld, codeNew, tel).enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                dialog.dismiss();
                bindNewPhoneDialog.dismiss();
                AccountPwBean data = JSONObject.toJavaObject(jsStr, AccountPwBean.class);
                SPUtils.getInstance(context, "bcSP").save(data, "");
                presenter.getView().onSuccess("");
                RoundView.getInstance().showSmallwin(context,listener,0);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //是否已完成实名认证
    public void IsRealName(Context context,BasePresenter presenter) {
        RetrofitManager.getInstance(context).getApiService().IsRealName().enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                presenter.getView().onSuccess("");
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //实名认证
    public void setRealName(Context context, String idCode, String realname, RealNameDialog realNameDialog,BasePresenter presenter) {

    }

    //用户在线
    public void isOnline(Context context) {
        RetrofitManager.getInstance(context).getApiService().isOnline().enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                OnlineBean onlineBean = JSONObject.toJavaObject(jsStr, OnlineBean.class);
                if (onlineBean.getData().getType().equals("off_line")){
                    UnderAgeDialog underAgeDialog = new UnderAgeDialog(context,onlineBean.getData().getMessage());
                    underAgeDialog.show();
                }

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
