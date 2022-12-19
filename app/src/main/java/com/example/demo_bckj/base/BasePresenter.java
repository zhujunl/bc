package com.example.demo_bckj.base;

import com.example.demo_bckj.inter.IBaseView;

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
}
