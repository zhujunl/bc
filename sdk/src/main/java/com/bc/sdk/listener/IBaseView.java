package com.bc.sdk.listener;

/**
 * @author WangKun
 * @description:
 * @date :2022/9/15 11:11
 */
public interface IBaseView {
    void onSuccess(Object o);
    void onError(String msg);
    void Login(boolean isAccount);
}
