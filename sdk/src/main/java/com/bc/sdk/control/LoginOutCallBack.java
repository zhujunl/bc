package com.bc.sdk.control;

/**
 * @author ZJL
 * @date 2023/2/6 15:50
 * @des
 * @updateAuthor
 * @updateDes
 */
public interface LoginOutCallBack {
    void onSuccess();

    void onFail(String message);
}
