package com.bc.sdk.listener;

/**
 * @author ZJL
 * @date 2022/12/14 13:52
 * @des 悬浮窗口点击监听接口
 * @updateAuthor
 * @updateDes
 */
public interface ClickListener {
    void CService(boolean isShow);

    void Personal(boolean isShow, boolean isAuthenticated);

    void Welfare(boolean isShow);

    void Switch();
}
