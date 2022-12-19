package com.example.demo_bckj.model;

import com.example.demo_bckj.model.bean.DateUpBean;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author WangKun
 * @description:  接口
 * @date :2022/9/15 11:20
 */
public interface ApiService {
    //数据上报
    @POST("sdk/v1/game/activation")
        Observable<DateUpBean> getSdk(@Header("sign") String sign,
                                      @Header("info") String info);

    //手机号登录(发送验证码)
    @GET("sdk/v1/sms/login")
    Observable<ResponseBody> getPhoneLoginCode(@Header("sign") String sign,
                                               @Header("info") String info,
                                               @Query("tel") String tel);
    //手机号登录
    @POST("sdk/v1/login/tel")
    Observable<ResponseBody> getPhoneLogin(@Header("sign") String sign,
                                           @Header("info") String info,
                                           @Query("tel")String tel,
                                           @Query("code")String code);

    //账号密码注册
    @POST("sdk/v1/register/pwd")
    Observable<ResponseBody> getLoginPwRe(@Header("sign") String sign,
                                           @Header("info") String info,
                                          @Query("account")String username,
                                          @Query("password")String password,
                                          @Query("password_confirmation")String password_confirmation);
    //账号密码登录
    @POST("sdk/v1/login")
    Observable<ResponseBody> getLoginPwLo(@Header("sign") String sign,
                                           @Header("info") String info,
                                           @Query("username")String username,
                                           @Query("password")String password);

    //随机账号密码
    @GET("sdk/v1/register/account")
    Observable<ResponseBody> getDemoAccount(@Header("sign") String sign,
                                            @Header("info") String info);
}
