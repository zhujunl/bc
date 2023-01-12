package com.example.demo_bckj.view.fragment;

import android.util.Log;

import com.example.demo_bckj.R;
import com.example.demo_bckj.base.BaseFragment;
import com.example.demo_bckj.presenter.WelfarePresenter;

/**
 * @author ZJL
 * @date 2022/12/14 14:44
 * @des 福利Fragment
 * @updateAuthor
 * @updateDes
 */
public class WelfareFragment extends BaseFragment<WelfarePresenter> {

    private final String TAG="WelfareFragment";

    public static WelfareFragment instance;

    public static WelfareFragment getInstance(){
        if (instance==null){
            instance=new WelfareFragment();
        }
        return instance;
    }

    public WelfareFragment() {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy" );
        instance=null;
    }
}
