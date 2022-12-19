package com.example.demo_bckj.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.demo_bckj.base.BasePresenter;
import com.example.demo_bckj.inter.ClickListener;
import com.example.demo_bckj.inter.PlayInterface;
import com.example.demo_bckj.model.RetrofitManager;
import com.example.demo_bckj.model.bean.AccountPwBean;
import com.example.demo_bckj.model.bean.DateUpBean;
import com.example.demo_bckj.model.bean.PlayBean;
import com.example.demo_bckj.model.bean.SignInfoBean;
import com.example.demo_bckj.model.utility.DeviceIdUtil;
import com.example.demo_bckj.model.utility.SPUtils;
import com.example.demo_bckj.view.round.RoundView;

import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * @author WangKun
 * @description:
 * @date :2022/9/23 15:16
 */
public class DemoPresenter extends BasePresenter {
    //数据上报
    public void getSdk(Context context){
        try {
            SignInfoBean signInfo = DeviceIdUtil.getSign(context);
            RetrofitManager.getInstance()
                    .getApiService()
                    .getSdk(signInfo.sign,signInfo.info)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<DateUpBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(DateUpBean value) {
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
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }
    //获取手机验证码
    public void getPhoneLoginCode(Context context,String tel){
        try {
            SignInfoBean signInfo = DeviceIdUtil.getSign(context);
            RetrofitManager.getInstance()
                    .getApiService()
                    .getPhoneLoginCode(signInfo.sign,signInfo.info,tel)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ResponseBody value) {
                            String responseBody = getResponseBody(value);
                            JSONObject jsStr = JSONObject.parseObject(responseBody);
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
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

    }
    //手机号登录
    public void getPhoneLogin(Context context, ClickListener listener, String tel, String code, AlertDialog dialog){
        SignInfoBean signInfo = null;
        try {
            signInfo = DeviceIdUtil.getSign(context);
            RetrofitManager.getInstance()
                    .getApiService()
                    .getPhoneLogin(signInfo.sign,signInfo.info,tel,code)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ResponseBody response) {
                                String responseBody = getResponseBody(response);
                                JSONObject jsStr = JSONObject.parseObject(responseBody);
                                AccountPwBean object = JSONObject.toJavaObject(jsStr,AccountPwBean.class);
                                if (object.getMessage().equals("OK")){
                                    dialog.dismiss();
                                    RoundView.getInstance().showRoundView(context,listener);
                                }else {
                                    Toast.makeText(context, object.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }
    //账号密码注册
    public void getLoginPwRe(Context context, String number, String pass, String pass2, AlertDialog alertDialog,ClickListener listener){
        try {
            SignInfoBean sign = DeviceIdUtil.getSign(context);
                        RetrofitManager.getInstance()
                    .getApiService()
                    .getLoginPwRe(sign.sign,sign.info,number,pass,pass2)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ResponseBody value) {
                            String responseBody = getResponseBody(value);
                            JSONObject jsStr = JSONObject.parseObject(responseBody);
                            Object code = jsStr.get("code");
                            if (code.toString().equals("0")){
                                SPUtils bcSP = SPUtils.getInstance(context, "bcSP");
                                AccountPwBean data = JSONObject.toJavaObject(jsStr,AccountPwBean.class);
                                bcSP.put("accountPW",data.getData().getAccount());
                                bcSP.put("tel",data.getData().getTel());
                                bcSP.put("slug",data.getData().getSlug());
                                bcSP.put("nick_name",data.getData().getNickName());
                                bcSP.put("is_authenticated",data.getData().getIsAuthenticated());
                                bcSP.put("realname",data.getData().getRealname());
                                bcSP.put("birthday",data.getData().getBirthday());
                                bcSP.put("age",data.getData().getAge());
                                bcSP.put("password",pass);
                                alertDialog.dismiss();
                                RoundView.getInstance().showRoundView(context,listener);
                            }else {
                                Toast.makeText(context, jsStr.get("message").toString(), Toast.LENGTH_SHORT).show();
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
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

    }
    //账号密码登录
    public void getLoginPwLo(Context context, EditText name,EditText password, AlertDialog dialog,ClickListener listener){
        try {
            SignInfoBean signInfo = DeviceIdUtil.getSign(context);
            RetrofitManager.getInstance()
                    .getApiService()
                    .getLoginPwLo(signInfo.sign,signInfo.info,name.getText().toString().trim(),password.getText().toString().trim())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ResponseBody value) {
                            String responseBody = getResponseBody(value);
                            JSONObject jsStr = JSONObject.parseObject(responseBody);
                            Object code = jsStr.get("code");
                            if (code.toString().equals("0")){
                                SPUtils bcSP = SPUtils.getInstance(context, "bcSP");
                                AccountPwBean data = JSONObject.toJavaObject(jsStr,AccountPwBean.class);
                                bcSP.put("accountPW",data.getData().getAccount());
                                bcSP.put("tel",data.getData().getTel());
                                bcSP.put("slug",data.getData().getSlug());
                                bcSP.put("nick_name",data.getData().getNickName());
                                bcSP.put("is_authenticated",data.getData().getIsAuthenticated());
                                bcSP.put("realname",data.getData().getRealname());
                                bcSP.put("birthday",data.getData().getBirthday());
                                bcSP.put("age",data.getData().getAge());
                                bcSP.put("password",password.getText().toString().trim());
                                dialog.dismiss();
                                RoundView.getInstance().showRoundView(context,listener);
                            }else {
                                Toast.makeText(context, jsStr.get("message").toString(), Toast.LENGTH_SHORT).show();
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
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    //快速试玩
    public void getDemoAccount(Context context, PlayInterface listener){
        try {
            SignInfoBean sign = DeviceIdUtil.getSign(context);
            RetrofitManager.getInstance()
                    .getApiService()
                    .getDemoAccount(sign.sign, sign.info)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ResponseBody value) {
                            String responseBody = getResponseBody(value);
                            JSONObject jsStr = JSONObject.parseObject(responseBody);
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
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

    }

    public static String getResponseBody(ResponseBody responseBody) {

        Charset UTF8 = Charset.forName("UTF-8");
        BufferedSource source = responseBody.source();
        try {
            source.request(Long.MAX_VALUE); // Buffer the entire body.
        } catch (IOException e) {
            e.printStackTrace();
        }
        Buffer buffer = source.buffer();

        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            try {
                charset = contentType.charset(UTF8);
            } catch (UnsupportedCharsetException e) {
                e.printStackTrace();
            }
        }
        return buffer.clone().readString(charset);
    }
}
