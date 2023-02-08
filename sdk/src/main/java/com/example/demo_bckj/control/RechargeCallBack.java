package com.example.demo_bckj.control;

/**
 * @author ZJL
 * @date 2023/2/6 15:37
 * @des
 * @updateAuthor
 * @updateDes
 */
public interface RechargeCallBack {
    void onSuccess(String orderNum);

    void onFail(String message);
}
