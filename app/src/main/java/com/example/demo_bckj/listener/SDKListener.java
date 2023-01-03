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
    void Login(Account account);

    void SignOut();

    void BindNewPhone(Account account);
}
