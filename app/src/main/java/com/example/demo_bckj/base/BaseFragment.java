package com.example.demo_bckj.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demo_bckj.inter.IBaseView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import me.jessyan.autosize.internal.CustomAdapt;

/**
 * @author WangKun
 * @description: Base
 * @date :2022/9/15 11:11
 */
public abstract class BaseFragment <P extends BasePresenter> extends Fragment implements IBaseView, CustomAdapt {
    protected P presenter;
    protected View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //初始化p层
        presenter=initPresenter();
        if (presenter!=null){
            presenter.attachView(this);
        }
         v = inflater.inflate(initLayoutID(), container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化控件和数据
        initView();
        initData();
    }
    /**
     * 是否按照宽度进行等比例适配 (为了保证在高宽比不同的屏幕上也能正常适配, 所以只能在宽度和高度之中选择一个作为基准进行适配)
     *
     * @return {@code true} 为按照宽度进行适配, {@code false} 为按照高度进行适配
     */
    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 1080;
    }
    protected abstract void initData();

    protected abstract void initView();

    protected abstract int initLayoutID();

    protected abstract P initPresenter();
    //防止内存泄露
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter!=null){
            presenter.detachView();
        }
    }
}
