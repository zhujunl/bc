package com.bc.sdk.base;

import android.os.Bundle;
import android.view.WindowManager;

import com.bc.sdk.listener.IBaseView;
import com.example.demo_bckj.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import me.jessyan.autosize.internal.CustomAdapt;

/**
 * @author WangKun
 * @description:
 * @date :2022/9/15 11:11
 */
public abstract class BaseActivity <P extends BasePresenter> extends AppCompatActivity implements IBaseView, CustomAdapt {
    protected P presenter;
    private FragmentManager fm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(initLayoutId());
        presenter=initPresenter();
        fm=getSupportFragmentManager();
        if (presenter!=null){
            presenter.attachView(this);
        }
        //初始化控件和布局
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

    protected abstract void initView();

    protected abstract void initData();

    protected abstract P initPresenter();

    protected abstract int initLayoutId();
    //防止内存泄露
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter!=null){
            presenter.detachView();
        }
    }

    public void nvTo(Fragment fragment){
        fm.beginTransaction()
                .replace(R.id.container,fragment)
                .addToBackStack(null)
                .commit();
    }

    public void nvTo(Fragment fragment,String name){
//        fm.beginTransaction()
//                .replace(R.id.container, fragment)
//                .addToBackStack(name)
//                .commit();
    }
}
