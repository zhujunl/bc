package com.bc.sdk.control;

import com.bc.sdk.model.bean.User;

/**
 * @author ZJL
 * @date 2023/2/6 15:41
 * @des
 * @updateAuthor
 * @updateDes
 */
public interface LoginCallBack {
    void onSuccess(User user);

    void onFail(String message);
}
