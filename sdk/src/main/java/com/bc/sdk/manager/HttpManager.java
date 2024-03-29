package com.bc.sdk.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.bc.sdk.R;
import com.bc.sdk.base.BasePresenter;
import com.bc.sdk.control.GameCallBack;
import com.bc.sdk.control.LoginCallBack;
import com.bc.sdk.control.LoginOutCallBack;
import com.bc.sdk.control.RechargeCallBack;
import com.bc.sdk.crash.CrashEntity;
import com.bc.sdk.db.entity.ConfigEntity;
import com.bc.sdk.listener.ClickListener;
import com.bc.sdk.listener.IBaseView;
import com.bc.sdk.listener.LogoutListener;
import com.bc.sdk.listener.PfRefreshCallBack;
import com.bc.sdk.listener.PlayInterface;
import com.bc.sdk.model.MyCallback;
import com.bc.sdk.model.RetrofitManager;
import com.bc.sdk.model.bean.AccountPwBean;
import com.bc.sdk.model.bean.AliPayBean;
import com.bc.sdk.model.bean.DateUpBean;
import com.bc.sdk.model.bean.OnlineBean;
import com.bc.sdk.model.bean.OrderBean;
import com.bc.sdk.model.bean.PayResult;
import com.bc.sdk.model.bean.PlayBean;
import com.bc.sdk.model.bean.RechargeOrder;
import com.bc.sdk.model.bean.RoleBean;
import com.bc.sdk.model.bean.URLBean;
import com.bc.sdk.model.bean.User;
import com.bc.sdk.model.bean.WXPayBean;
import com.bc.sdk.model.request.BindPhoneRequest;
import com.bc.sdk.model.request.LoginPwLoRequest;
import com.bc.sdk.model.request.LoginPwReRequest;
import com.bc.sdk.model.request.ModifyBindPhoneRequest;
import com.bc.sdk.model.request.ModifyPwdRequest;
import com.bc.sdk.model.request.PhoneLoginRequest;
import com.bc.sdk.model.request.RealNameRequest;
import com.bc.sdk.model.request.ResetPwdRequest;
import com.bc.sdk.model.request.SDkRequest;
import com.bc.sdk.model.request.WXPayRequest;
import com.bc.sdk.model.utility.CountDownTimerUtils;
import com.bc.sdk.model.utility.FileUtil;
import com.bc.sdk.model.utility.SPUtils;
import com.bc.sdk.model.utility.ToastUtil;
import com.bc.sdk.presenter.HomePresenter;
import com.bc.sdk.service.TimeService;
import com.bc.sdk.view.Constants;
import com.bc.sdk.view.activity.WXPayActivity;
import com.bc.sdk.view.dialog.BindNewPhoneDialog;
import com.bc.sdk.view.dialog.ModifyPWDialog;
import com.bc.sdk.view.dialog.RealNameDialog;
import com.bc.sdk.view.dialog.RealNameRegisterDialog;
import com.bc.sdk.view.dialog.RechargeDialog;
import com.bc.sdk.view.dialog.UnderAgeDialog;
import com.bc.sdk.view.dialog.VerifyPhoneDialog;
import com.bc.sdk.view.round.RoundView;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
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
    private LoginCallBack loginListener;
    private LoginOutCallBack loginOutListener;
    private LogoutListener logoutListener;
    private ClickListener listener;
    private RechargeCallBack rechargeListener;
    private Context context;
    private HomePresenter homePresenter;
    private RechargeOrder rechargeOrder;
    private PfRefreshCallBack refreshCallback;

    private static HttpManager instance;

    public static HttpManager getInstance() {
        if (instance == null) {
            instance = new HttpManager();
        }
        return instance;
    }

    public HttpManager() {
    }

    public void init(LoginCallBack loginListener, LoginOutCallBack loginOutListener) {
        this.loginListener = loginListener;
        this.loginOutListener = loginOutListener;
    }

    private RechargeCallBack getRechargeCallBack() {
        return rechargeListener;
    }

    public void setRefreshCallback(PfRefreshCallBack refreshCallback) {
        this.refreshCallback = refreshCallback;
    }

    public void setRecharge() {
        mHandlerThread = new HandlerThread("payOrder");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                        /**
                         * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                         */
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                        String resultStatus = payResult.getResultStatus();
                        // 判断resultStatus 为9000则代表支付成功
                        if (TextUtils.equals(resultStatus, "9000")) {
                            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                            if (getRechargeCallBack() != null)
                                getRechargeCallBack().onSuccess(context.getString(R.string.orderNum) + getRechargeOrder().getNumber_game());
                        } else {
                            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                            if (getRechargeCallBack() != null)
                                getRechargeCallBack().onFail(context.getString(R.string.orderNum) + getRechargeOrder().getNumber_game() + context.getString(R.string.pay_failed) + payResult.getMemo());
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public RechargeOrder getRechargeOrder() {
        return rechargeOrder;
    }

    public void setRechargeOrder(RechargeOrder rechargeOrder) {
        this.rechargeOrder = rechargeOrder;
    }

    public void setListener(ClickListener clickListener, LogoutListener logoutListener, Context context) {
        this.listener = clickListener;
        this.logoutListener = logoutListener;
        this.context = context;
    }

    public void setHomePresenter(HomePresenter homePresenter) {
        this.homePresenter = homePresenter;
    }

    public boolean init(Context context) throws IOException {
        Response<ResponseBody> execute = RetrofitManager.getInstance(context).getApiService().init().execute();
        ResponseBody body = execute.body();
        JSONObject json = FileUtil.getResponseBody(body);
        if (json == null)
            return false;
        Object code = json.get("code");
        if (code.toString().equals("0")) {
            Object data = json.get("data");
            JSONObject jsonObject = JSONObject.parseObject(data.toString());
            URLBean urlBean = JSONObject.toJavaObject(jsonObject, URLBean.class);
            Constants.DEVICE = urlBean.getProtocol_url().getDevice();
            Constants.REGISTER = urlBean.getProtocol_url().getRegister();
            Constants.PRIVACY = urlBean.getProtocol_url().getPrivacy();
            Constants.CUSTOMER_SERVICE = urlBean.getUrl().getCustomer_service();
            return true;
        } else {
            return false;
        }
    }

    //数据上报
    public void getSdk(Context context) {
        RetrofitManager.getInstance(context)
                .getApiService()
                .getSdk()
                .enqueue(new MyCallback<ResponseBody>() {
                    @Override
                    public void onSuccess(JSONObject jsStr) {
                        Object data = jsStr.get("data");
                        JSONObject jsonObject = JSONObject.parseObject(data.toString());
                        String id = jsonObject.get("id").toString();
                        SPUtils.getInstance(context, "bcSP").put("id", Integer.valueOf(id));
                    }

                    @Override
                    public void onError(String message) {

                    }
                });

    }

    //数据上报
    public void getSdk(Context context,  int id) {
        SDkRequest sDkRequest = new SDkRequest(id);
        RetrofitManager.getInstance(context)
                .getApiService()
                .getSdk(sDkRequest)
                .enqueue(new MyCallback<ResponseBody>() {
                    @Override
                    public void onSuccess(JSONObject jsStr) {
                    }

                    @Override
                    public void onError(String message) {
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
                        ToastUtil.show(context, "获取验证码失败，" + message);
                        IBaseView view = presenter.getView();
                        if (view != null) {
                            view.onError(message);
                        }
                    }
                });

    }

    //手机号登录
    public void getPhoneLogin(Context context, String tel, String code) {
        PhoneLoginRequest phoneLoginRequest = new PhoneLoginRequest(tel, code);
        RetrofitManager.getInstance(context)
                .getApiService()
                .getPhoneLogin(phoneLoginRequest).enqueue(new MyCallback<ResponseBody>(context) {
                    @Override
                    public void onSuccess(JSONObject jsStr) {
                        AccountPwBean data = JSONObject.toJavaObject(jsStr, AccountPwBean.class);
                        DBManager.getInstance(context).insertAccount(data, "");
                        ConfigEntity authorization = DBManager.getInstance(context).getAuthorization();
                        User user = new User.Builder()
                                .slug(data.getData().getSlug())
                                .isAuthenticated(data.getData().getAuthenticated())
                                .age(data.getData().getAge())
                                .token(authorization.getAuthorization())
                                .build();
                        loginListener.onSuccess(user);
                        TimeService.start(context);
                        DBManager.getInstance(context).insertTel(tel);
                        SPUtils.getInstance(context, "bcSP").put("isAccount", false);
                        RoundView.getInstance().showRoundView(context, listener);
                        refreshCallback.refresh();
                        listener.Personal(false, data.getData().getAuthenticated());
                    }

                    @Override
                    public void onError(String message) {
                        ToastUtil.show(context, "登录失败，" + message);
                        loginListener.onFail("登录失败，" + message);
                    }
                });
    }

    //账号密码注册
    public void getLoginPwRe(Context context, String number, String pass, String pass2, AlertDialog alertDialog, Thread thread) {
        LoginPwReRequest loginPwReRequest = new LoginPwReRequest(number, pass, pass2);
        RetrofitManager.getInstance(context)
                .getApiService()
                .getLoginPwRe(loginPwReRequest).enqueue(new MyCallback<ResponseBody>(context) {
                    @Override
                    public void onSuccess(JSONObject jsStr) {
                        AccountPwBean data = JSONObject.toJavaObject(jsStr, AccountPwBean.class);
                        DBManager.getInstance(context).insertAccount(data, pass);
                        if (data.getData().getAuthenticated())
                            TimeService.start(context);
                        ConfigEntity authorization = DBManager.getInstance(context).getAuthorization();
                        User user = new User.Builder()
                                .slug(data.getData().getSlug())
                                .isAuthenticated(data.getData().getAuthenticated())
                                .age(data.getData().getAge())
                                .token(authorization.getAuthorization())
                                .build();
                        loginListener.onSuccess(user);
                        DBManager.getInstance(context).insertAccount(number, pass);
                        SPUtils.getInstance(context, "bcSP").put("isAccount", true);
                        alertDialog.dismiss();
                        RoundView.getInstance().showRoundView(context, listener);
                        thread.start();
                        refreshCallback.refresh();
                        listener.Personal(false, data.getData().getAuthenticated());
                    }

                    @Override
                    public void onError(String message) {
                        ToastUtil.show(context, "登录失败，" + message);
                        loginListener.onFail("登录失败，" + message);
                    }
                });
    }

    //账号密码登录
    public void getLoginPwLo(Context context, String name, String password) {
        LoginPwLoRequest loginPwLoRequest = new LoginPwLoRequest(name, password);
        RetrofitManager.getInstance(context)
                .getApiService()
                .getLoginPwLo(loginPwLoRequest).enqueue(new MyCallback<ResponseBody>(context) {
                    @Override
                    public void onSuccess(JSONObject jsStr) {
                        AccountPwBean data = JSONObject.toJavaObject(jsStr, AccountPwBean.class);
                        DBManager.getInstance(context).insertAccount(data, password);
                        TimeService.start(context);
                        ConfigEntity authorization = DBManager.getInstance(context).getAuthorization();
                        User user = new User.Builder()
                                .slug(data.getData().getSlug())
                                .isAuthenticated(data.getData().getAuthenticated())
                                .age(data.getData().getAge())
                                .token(authorization.getAuthorization())
                                .build();
                        loginListener.onSuccess(user);
                        SPUtils.getInstance(context, "bcSP").put("isAccount", true);
                        DBManager.getInstance(context).insertAccount(name, password);
                        RoundView.getInstance().showRoundView(context, listener);
                        refreshCallback.refresh();
                        listener.Personal(false, data.getData().getAuthenticated());
                    }

                    @Override
                    public void onError(String message) {
                        ToastUtil.show(context, "登录失败，" + message);
                        loginListener.onFail("登录失败，" + message);
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
                        ToastUtil.show(context, "快速生成账号密码失败，" + message);
                    }
                });


    }


    //忘记密码
    public void forgetPwd(Context context, String number, TextView popupTvCode) {
        RetrofitManager.getInstance(context).getApiService().forgetPwd(number).enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                ToastUtil.show(context, "发送成功");
                CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(popupTvCode, 60000, 1000);
                countDownTimerUtils.start();
            }

            @Override
            public void onError(String message) {
                ToastUtil.show(context, message);
            }
        });
    }

    //重置密码
    public void resetPwd(Context context, String tel, String code, String password, String passwordConfirmation, Dialog dialog, boolean isLogin) {
        ResetPwdRequest resetPwdRequest = new ResetPwdRequest(tel, code, password, passwordConfirmation);
        RetrofitManager.getInstance(context).getApiService().resetPwd(resetPwdRequest).enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                Object message = jsStr.get("message");
                ToastUtil.show(context, message.toString());
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (isLogin) {
                    loginOut(context, false, true);
                } else {
                    logoutListener.out(true);
                }
            }

            @Override
            public void onError(String message) {
                ToastUtil.show(context, message);
            }
        });
    }

    //实名认证
    public void setRealName(Context context, String idCode, String realName, RealNameDialog dialog, PfRefreshCallBack callBack) {
        RealNameRequest realNameRequest = new RealNameRequest(idCode, realName);
        RetrofitManager.getInstance(context).getApiService().setRealName(realNameRequest).enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                AccountPwBean data = JSONObject.toJavaObject(jsStr, AccountPwBean.class);
                DBManager.getInstance(context).insertAccount(data, "");
                Integer age = data.getData().getAge();
                dialog.dismiss();
                TimeService.start(context);
                if (!(age < 18)) {
                    RealNameRegisterDialog r = new RealNameRegisterDialog(context);
                    r.show();
                }
                if (callBack!=null)
                    callBack.refresh();
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
            DBManager.getInstance(context).insertAuthorization(Authorization);
            JSONObject json = FileUtil.getResponseBody(execute.body());
            Object code = json.get("code");
            if (code.toString().equals("0")) {
                AccountPwBean data = JSONObject.toJavaObject(json, AccountPwBean.class);
                DBManager.getInstance(context).insertAccount(data, "");
                TimeService.start(context);
                ConfigEntity authorization = DBManager.getInstance(context).getAuthorization();
                User user = new User.Builder()
                        .slug(data.getData().getSlug())
                        .isAuthenticated(data.getData().getAuthenticated())
                        .age(data.getData().getAge())
                        .token(authorization.getAuthorization())
                        .build();
                loginListener.onSuccess(user);
                refreshCallback.refresh();
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
                ToastUtil.show(context, "发送成功");
                CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(TCode, 60000, 1000);
                countDownTimerUtils.start();
            }

            @Override
            public void onError(String message) {
                ToastUtil.show(context, message);
            }
        });
    }

    //绑定手机
    public void BindPhone(Context context, String tel, String code, BindNewPhoneDialog bindNewPhoneDialog, BasePresenter presenter) {
        BindPhoneRequest bindPhoneRequest = new BindPhoneRequest(tel, code);
        RetrofitManager.getInstance(context).getApiService().BindPhone(bindPhoneRequest).enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                bindNewPhoneDialog.dismiss();
                DBManager.getInstance(context).BindPhone(tel);
                ToastUtil.show(context, "绑定成功");
                refreshCallback.refresh();
            }

            @Override
            public void onError(String message) {
                ToastUtil.show(context, message);
            }
        });
    }

    //换绑手机-发送验证码到原手机
    public void modifyBind(Context context, TextView TCode) {
        RetrofitManager.getInstance(context).getApiService().modifyBind().enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                ToastUtil.show(context, "发送成功");
                CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(TCode, 60000, 1000);
                countDownTimerUtils.start();
            }

            @Override
            public void onError(String message) {
                ToastUtil.show(context, message);
            }
        });
    }

    //换绑手机-发送验证码到新手机
    public void modifyBindCode(Context context, String tel, TextView TCode) {
        RetrofitManager.getInstance(context).getApiService().modifyBindCode(tel).enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                ToastUtil.show(context, "发送成功");
                CountDownTimerUtils countDownTimerUtils = new CountDownTimerUtils(TCode, 60000, 1000);
                countDownTimerUtils.start();
            }

            @Override
            public void onError(String message) {
                ToastUtil.show(context, message);
            }
        });
    }

    //换绑手机-换绑手机
    public void modifyBindPhone(Context context, String codeOld, String codeNew, String tel, VerifyPhoneDialog dialog,
                                BindNewPhoneDialog bindNewPhoneDialog, BasePresenter presenter) {
        ModifyBindPhoneRequest modifyBindPhoneRequest = new ModifyBindPhoneRequest(codeOld, codeNew, tel);
        RetrofitManager.getInstance(context).getApiService().modifyBindPhone(modifyBindPhoneRequest).enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                dialog.dismiss();
                bindNewPhoneDialog.dismiss();
                AccountPwBean data = JSONObject.toJavaObject(jsStr, AccountPwBean.class);
                DBManager.getInstance(context).insertAccount(data, "");
                refreshCallback.refresh();
                RoundView.getInstance().showSmallwin(context, listener, 0);
            }

            @Override
            public void onError(String message) {
                ToastUtil.show(context, message);
            }
        });
    }


    //用户在线
    public void isOnline(Context c) {
        RetrofitManager.getInstance(c).getApiService().isOnline().enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                if (!TimeService.isRun()) {
                    return;
                }
                OnlineBean onlineBean = JSONObject.toJavaObject(jsStr, OnlineBean.class);
                if (onlineBean.getData().getType().equals("off_line")) {
                    UnderAgeDialog underAgeDialog = new UnderAgeDialog(context, onlineBean.getData().getMessage());
                    underAgeDialog.show();
                }
            }

            @Override
            public void onError(String message) {
                if (!TimeService.isRun()) {
                    return;
                }
                ToastUtil.show(context, message);
            }
        });
    }

    //修改密码
    public void modifyPwd(Context context, String passwordOld, String password, String passwordConfirmation, ModifyPWDialog modifyPWDialog) {
        ModifyPwdRequest modifyPwdRequest = new ModifyPwdRequest(passwordOld, password, passwordConfirmation);
        RetrofitManager.getInstance(context).getApiService().modifyPwd(modifyPwdRequest).enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                modifyPWDialog.dismiss();
                ToastUtil.show(context, "密码修改成功");
                loginOut(context, false, true);
            }

            @Override
            public void onError(String message) {
                ToastUtil.show(context, message);
            }
        });
    }

    //退出
    public void loginOut(Context context, boolean isDestroy, boolean isLoginShow) {
        RetrofitManager.getInstance(context).getApiService().logout().enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                TimeService.stop(context);
                loginOutListener.onSuccess();
                DBManager.getInstance(context).delete();
                if (isDestroy)
                    System.exit(0);
                if (logoutListener != null) {
                    logoutListener.out(isLoginShow);
                }
            }

            @Override
            public void onError(String message) {
                loginOutListener.onFail("退出失败，" + message);
            }
        });
    }


    public void loginOut(Context context, boolean isDestroy, boolean isLoginShow, LoginOutCallBack outListener) {
        if (outListener != null)
            this.loginOutListener = outListener;
        RetrofitManager.getInstance(context).getApiService().logout().enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                TimeService.stop(context);
                loginOutListener.onSuccess();
                DBManager.getInstance(context).delete();
                if (isDestroy)
                    System.exit(0);
                if (logoutListener != null) {
                    logoutListener.out(isLoginShow);
                }
            }

            @Override
            public void onError(String message) {
                loginOutListener.onFail("退出失败，" + message);
            }
        });
    }

    /**
     * 登录
     */
    public void Login(Context context, LoginCallBack loginListener) {
        if (loginListener != null)
            this.loginListener = loginListener;
        if (homePresenter != null) {
            homePresenter.Login(context,loginListener);
        }
    }

    private HandlerThread mHandlerThread;
    private Handler mHandler;

    //支付宝支付
    public void charge(Activity context, @NonNull RechargeOrder rechargeOrder, RechargeCallBack rechargeListener) {
        if (rechargeListener != null)
            this.rechargeListener = rechargeListener;
        setRechargeOrder(rechargeOrder);
        try {
            Response<ResponseBody> execute = RetrofitManager.getInstance(context).getApiService().CreateOrder(rechargeOrder).execute();
            JSONObject json = FileUtil.getResponseBody(execute.body());
            if (json.get("code").toString().equals("0")) {
                OrderBean response = JSONObject.toJavaObject(json, OrderBean.class);
                String orderNum = response.getData().getNumber();
                Response<ResponseBody> aliExecute = RetrofitManager.getInstance(context).getApiService().AliPay(orderNum).execute();
                JSONObject aliJson = FileUtil.getResponseBody(aliExecute.body());
                if (aliJson.get("code").toString().equals("0")) {
                    AliPayBean aliResponse = JSONObject.toJavaObject(aliJson, AliPayBean.class);
                    String content = aliResponse.getData().getContent();
                    PayTask alipay = new PayTask(context);
                    Map<String, String> result = alipay.payV2(content, true);
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                    Log.i("msp", result.toString());
                } else if (aliJson.get("code").toString().equals("1")) {
                    Object data = aliJson.get("data");
                    JSONObject jsonObject = JSONObject.parseObject(data.toString());
                    Object tip = jsonObject.get("tip");
                    Object link = jsonObject.get("link");
                    Looper.prepare();
                    RechargeDialog rechargeDialog = new RechargeDialog(context, aliJson.get("message").toString(), tip.toString(), link.toString());
                    rechargeDialog.show();
                    Looper.loop();
                } else {
                    if (this.rechargeListener != null)
                        this.rechargeListener.onFail(aliJson.get("message").toString());
                }
            } else if (json.get("code").toString().equals("1")) {
                Looper.prepare();
                RechargeDialog rechargeDialog = new RechargeDialog(context, json.get("message").toString().replaceAll("\\\\n", "\n"));
                rechargeDialog.show();
                Looper.loop();
            } else {
                if (this.rechargeListener != null)
                    this.rechargeListener.onFail(json.get("message").toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //微信支付
    public void WXCharge(Activity context, @NonNull RechargeOrder rechargeOrder, RechargeCallBack rechargeListener) {
        if (rechargeListener != null)
            this.rechargeListener = rechargeListener;
        setRechargeOrder(rechargeOrder);
        try {
            Response<ResponseBody> execute = RetrofitManager.getInstance(context).getApiService().CreateOrder(rechargeOrder).execute();
            JSONObject json = FileUtil.getResponseBody(execute.body());
            assert json != null;
            if (Objects.requireNonNull(json.get("code")).toString().equals("0")) {
                OrderBean response = JSONObject.toJavaObject(json, OrderBean.class);
                String orderNum = response.getData().getNumber();
                WXPayRequest wxPayRequest=new WXPayRequest(orderNum,"weixin");
                Response<ResponseBody> wxPay = RetrofitManager.getInstance(context).getApiService().WXPay(wxPayRequest).execute();
                JSONObject wxJson = FileUtil.getResponseBody(wxPay.body());
                assert wxJson != null;
                if (Objects.requireNonNull(wxJson.get("code")).toString().equals("0")){
                    WXPayBean wxPayBean = JSONObject.toJavaObject(wxJson, WXPayBean.class);
                    PackageManager packageManager = context.getPackageManager();
                    Intent intent=new Intent(context, WXPayActivity.class);
                    intent.putExtra("url",wxPayBean.getData().getUrl());
                    context.startActivity(intent);
                }
            } else if (Objects.requireNonNull(json.get("code")).toString().equals("1")) {
                Looper.prepare();
                RechargeDialog rechargeDialog = new RechargeDialog(context, json.get("message").toString().replaceAll("\\\\n", "\n"));
                rechargeDialog.show();
                Looper.loop();
            } else {
                if (this.rechargeListener != null)
                    this.rechargeListener.onFail(json.get("message").toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //用户创角
    public void CreateRole(Context context, RoleBean roleBean, GameCallBack callback) {
        RetrofitManager.getInstance(context).getApiService().CreateRole(roleBean).enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                if (callback != null)
                    callback.onSuccess();
            }

            @Override
            public void onError(String message) {
                if (callback != null)
                    callback.onFail(message);
            }
        });
    }

    //角色登录区服
    public void LoginServer(Context context, RoleBean roleBean, GameCallBack callback) {
        RetrofitManager.getInstance(context).getApiService().LoginServer(roleBean).enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {
                if (callback != null)
                    callback.onSuccess();
            }

            @Override
            public void onError(String message) {
                if (callback != null)
                    callback.onFail(message);
            }
        });
    }

    //崩溃日志上报
    public void CrashLog(Context context, CrashEntity log) {
        RetrofitManager.getInstance(context).getApiService().CrashLog(log).enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {

            }

            @Override
            public void onError(String message) {

            }
        });
    }
}
