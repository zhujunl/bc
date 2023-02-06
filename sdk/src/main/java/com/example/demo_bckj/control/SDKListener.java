package com.example.demo_bckj.control;

import com.example.demo_bckj.model.bean.User;

/**
 * @author ZJL
 * @date 2022/12/26 9:47
 * @des
 * @updateAuthor
 * @updateDes
 */
public interface SDKListener {

    /**
     * 登录
     * @param user
     */
    void Login(User user);

    void LoginFail(String message);

    /**
     * 退出
     */
    void SignOut();

    void SignOutFail(String message);

    /**
     * 充值成功
     * @param orderNum 订单号
     */
    void RechargeSuccess(String orderNum);

    /**
     * 充值失败
     * @param message 失败信息
     */
    void RechargeFail(String message);
}
