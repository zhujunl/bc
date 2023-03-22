package com.bc.sdk.model;

import com.bc.sdk.crash.CrashEntity;
import com.bc.sdk.model.bean.RechargeOrder;
import com.bc.sdk.model.bean.RoleBean;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author WangKun
 * @description:  接口
 * @date :2022/9/15 11:20
 */
public interface ApiService {
    //初始化
    //{"code":0,"message":"OK","data":
    // {"protocol_url":{
    // "register":"https://static.infinite-game.cn/resources/protocol/register.html",
    // "privacy":"https://static.infinite-game.cn/resources/protocol/device.html",
    // "device":"https://static.infinite-game.cn/resources/protocol/device.html"}
    @GET("sdk/v1/game/init")
    Call<ResponseBody> init();

    /**=========================================注册登录======================================================================*/
    //数据上报
    @POST("sdk/v1/game/activation")
    Call<ResponseBody> getSdk();

    @POST("sdk/v1/game/activation")
    Call<ResponseBody> getSdk(@Body SDkRequest SDkRequest);

    //手机号登录(发送验证码)
    @GET("sdk/v1/sms/login")
    Call<ResponseBody> getPhoneLoginCode(@Query("tel") String tel);
    //手机号登录
    @POST("sdk/v1/login/tel")
    Call<ResponseBody> getPhoneLogin(@Body PhoneLoginRequest phoneLoginRequest);

    //账号密码注册
    @POST("sdk/v1/register/pwd")
    Call<ResponseBody> getLoginPwRe(@Body LoginPwReRequest loginPwReRequest);
    //账号密码登录
    @POST("sdk/v1/login")
    Call<ResponseBody> getLoginPwLo(@Body LoginPwLoRequest loginPwLoRequest);

    //随机账号密码
    @GET("sdk/v1/register/account")
    Call<ResponseBody> getDemoAccount();

    //刷新token
    @GET("sdk/v1/token/refresh")
    Call<ResponseBody> refreshToken();

    //登出
    @GET("sdk/v1/logout")
    Call<ResponseBody> logout();

    /**=========================================用户信息======================================================================*/

    //是否已完成实名认证
    @GET("sdk/v1/user/realname/finish")
    Call<ResponseBody> IsRealName();
    //实名认证
    @POST("sdk/v1/user/realname")
    Call<ResponseBody> setRealName(@Body RealNameRequest realNameRequest);
    //用户在线
    @GET("sdk/v1/user/online")
    Call<ResponseBody> isOnline();
    //修改密码
    @POST("sdk/v1/user/pwd")
    Call<ResponseBody> modifyPwd(@Body ModifyPwdRequest modifyPwdRequest);
    //忘记密码
    @GET("sdk/v1/sms/pwd")
    Call<ResponseBody> forgetPwd(@Query("tel") String number);
    //重置密码
    @POST("/sdk/v1/user/pwd/reset")
    Call<ResponseBody> resetPwd(@Body ResetPwdRequest resetPwdRequest);
    //绑定手机-发送验证码
    @GET("sdk/v1/sms/user/tel")
    Call<ResponseBody> BindPhoneCode(@Query("tel") String tel);
    //绑定手机-绑定手机
    @POST("sdk/v1/user/tel")
    Call<ResponseBody> BindPhone(@Body BindPhoneRequest bindPhoneRequest);
    //换绑手机-发送验证码到原手机
    @GET("sdk/v1/sms/user/tel/old")
    Call<ResponseBody> modifyBind();
    //换绑手机-发送验证码到新手机
    @GET("sdk/v1/sms/user/tel/new")
    Call<ResponseBody> modifyBindCode(@Query("tel") String tel);
    //换绑手机-换绑手机
    @POST("sdk/v1/user/tel/modify")
    Call<ResponseBody> modifyBindPhone(@Body ModifyBindPhoneRequest modifyBindPhoneRequest);


    /**=========================================订单信息======================================================================*/
    //创建订单
    @POST("sdk/v1/order")
    Call<ResponseBody> CreateOrder(@Body RechargeOrder rechargeOrder);
    //支付宝-原生支付
    @POST("sdk/v1/order/pay/ali/app")
    Call<ResponseBody> AliPay(@Query("number") String number);
    @POST("sdk/v1/order/pay/ali/app")
    Call<ResponseBody> AliPay(@Query("number") String number,
                              @Query("exception") boolean exception);

    //微信-扫码支付
    @POST("sdk/v1/order/pay/wc/h5")
    Call<ResponseBody> WXPay(@Body WXPayRequest request);
    //支付宝-手机网站（h5）支付
    @POST("sdk/v1/order/pay/ali/wap")
    Call<ResponseBody> AliH5Pay(@Query("number") String number,
                                @Query("type") String type);


    /****************************************游戏角色*******************************************************/
    //用户创角
    @POST("sdk/v1/role")
    Call<ResponseBody> CreateRole(@Body RoleBean request);
    //角色登录区服
    @POST("sdk/v1/role/login")
    Call<ResponseBody> LoginServer(@Body RoleBean request);

    //崩溃日志
    @POST("sdk/v1/crash")
    Call<ResponseBody> CrashLog(@Body CrashEntity entity);
}
