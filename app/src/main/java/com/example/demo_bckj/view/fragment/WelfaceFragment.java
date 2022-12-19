package com.example.demo_bckj.view.fragment;

import com.example.demo_bckj.R;
import com.example.demo_bckj.base.BaseFragment;
import com.example.demo_bckj.presenter.WelfarePresenter;

/**
 * @author ZJL
 * @date 2022/12/14 14:44
 * @des
 * @updateAuthor
 * @updateDes
 */
public class WelfaceFragment extends BaseFragment<WelfarePresenter> {

    public static WelfaceFragment instance;

    public static WelfaceFragment getInstance(){
        if (instance==null){
            instance=new WelfaceFragment();
        }
        return instance;
    }

    public WelfaceFragment() {
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected int initLayoutID() {
        return R.layout.fragment_welfare;
    }

    @Override
    protected WelfarePresenter initPresenter() {
        return null;
    }

    @Override
    public void onSuccess(Object o) {

    }

    @Override
    public void onError(String msg) {

    }
}
