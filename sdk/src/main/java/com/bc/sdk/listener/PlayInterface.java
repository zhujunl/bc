package com.bc.sdk.listener;

/**
 * @author ZJL
 * @date 2022/12/19 11:31
 * @des
 * @updateAuthor
 * @updateDes
 */
public interface PlayInterface {
    void onSuccess(String account,String password);
    void onError(String msg);
}
