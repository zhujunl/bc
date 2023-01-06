package com.example.demo_bckj.listener;

import com.example.demo_bckj.model.bean.Account;

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
     *
     * @param account
     */
    void Login(Account account);

    /**
     * 退出
     */
    void SignOut();

    /**
     * 充值成功
     */
    void RechargeSuccess();

    /**
     * 充值失败
     *
     * @param message
     */
    void RechargeFail(String message);
}
