package com.example.demo_bckj.model;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author WangKun
 * @description:  接口
 * @date :2022/9/15 11:20
 */
public interface ApiService {

    /**=========================================注册登录======================================================================*/
    //数据上报
    @POST("sdk/v1/game/activation")
    Observable<ResponseBody> getSdk();

    //手机号登录(发送验证码)
    @GET("sdk/v1/sms/login")
    Observable<ResponseBody> getPhoneLoginCode(@Query("tel") String tel);
    //手机号登录
    @POST("sdk/v1/login/tel")
    Call<ResponseBody> getPhoneLogin(@Query("tel")String tel,
                                           @Query("code")String code);

    //账号密码注册
    @POST("sdk/v1/register/pwd")
    Call<ResponseBody> getLoginPwRe(@Query("account")String username,
                                          @Query("password")String password,
                                          @Query("password_confirmation")String password_confirmation);
    //账号密码登录
    @POST("sdk/v1/login")
    Call<ResponseBody> getLoginPwLo(@Query("username")String username,
                                          @Query("password")String password);
//    Observable<ResponseBody> getLoginPwLo(@Query("username")String username,
//                                           @Query("password")String password);

    //随机账号密码
    @GET("sdk/v1/register/account")
    Observable<ResponseBody> getDemoAccount();

    //登出
    @GET("sdk/v1/logout")
    Call<ResponseBody> logout();

    /**=========================================用户信息======================================================================*/

    //是否已完成实名认证
    @GET("sdk/v1/user/realname/finish")
    Call<ResponseBody> IsRealName();
    //实名认证
    @POST("sdk/v1/user/realname")
    Call<ResponseBody> setRealName(@Query("id_code") String idCode,
                                   @Query("realname") String realname);
    //用户在线
    @GET("sdk/v1/user/online")
    Call<ResponseBody> isOnline();
    //修改密码
    @POST("sdk/v1/user/pwd")
    Call<ResponseBody> modifyPwd(@Query("password_old") String passwordOld,
                                 @Query("password") String password,
                                 @Query("password_confirmation") String passwordConfirmation);
    //忘记密码
    @GET("sdk/v1/sms/pwd")
    Call<ResponseBody> forgetPwd(@Query("tel") String number);
    //重置密码
    @POST("sdk/v1/sms/pwd")
    Call<ResponseBody> resetPwd(@Query("tel") String tel,
                                @Query("code") String code,
                                @Query("password") String password,
                                @Query("password_confirmation") String passwordConfirmation);
    //绑定手机-发送验证码
    @GET("sdk/v1/sms/user/tel")
    Call<ResponseBody> BindPhoneCode(@Query("tel") String tel);
    //绑定手机-绑定手机
    @POST("sdk/v1/user/tel")
    Call<ResponseBody> BindPhone(@Query("tel") String tel,
                                 @Query("code") String code);
    //换绑手机-发送验证码到原手机
    @GET("sdk/v1/sms/user/tel/old")
    Call<ResponseBody> modifyBind();
    //换绑手机-发送验证码到新手机
    @GET("sdk/v1/sms/user/tel/new")
    Call<ResponseBody> modifyBindCode(@Query("tel") String tel);
    //换绑手机-换绑手机
    @POST("sdk/v1/user/tel/modify")
    Call<ResponseBody> modifyBindPhone(@Query("code_old") String codeOld,
                                       @Query("code_new") String codeNew,
                                       @Query("tel") String tel);

}
