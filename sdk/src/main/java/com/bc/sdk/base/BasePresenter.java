package com.bc.sdk.base;

import com.bc.sdk.listener.IBaseView;

/**
 * @author WangKun
 * @description:
 * @date :2022/9/15 11:15
 */
public class BasePresenter <V extends IBaseView>{
    protected V view;

    public void attachView(V view) {
        this.view=view;
    }

    public void detachView() {
        this.view=null;
    }

    public V getView() {
        return view;
    }
}
