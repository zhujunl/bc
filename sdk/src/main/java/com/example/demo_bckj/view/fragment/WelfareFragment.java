package com.example.demo_bckj.view.fragment;

import android.content.Context;
import android.util.Log;

import com.example.demo_bckj.R;
import com.example.demo_bckj.base.BaseFragment;
import com.example.demo_bckj.control.SDKListener;
import com.example.demo_bckj.presenter.WelfarePresenter;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

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

    private  DrawerLayout drawerLayout;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    public static WelfareFragment getInstance(SDKListener sdkListener, DrawerLayout drawerLayout){
        if (instance==null){
            instance=new WelfareFragment(drawerLayout);
        }
        return instance;
    }

    public WelfareFragment(DrawerLayout drawerLayout) {
        this.drawerLayout=drawerLayout;
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
    public void Login(boolean isAccount) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy" );
        instance=null;
    }

    protected void finish() {
        if (fm.getBackStackEntryCount()>1){
            fm.popBackStack("Welfare", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            drawerLayout.closeDrawers();
            return;
        }
        System.exit(0);
    }
}
