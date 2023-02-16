package com.example.demo_bckj.view.fragment;

import android.content.Context;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.demo_bckj.R;
import com.example.demo_bckj.base.BaseFragment;
import com.example.demo_bckj.presenter.CServicePresenter;
import com.example.demo_bckj.view.Constants;

import java.util.HashMap;
import java.util.Map;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

/**
 * @author ZJL
 * @date 2022/12/14 14:44
 * @des 客服Fragment
 * @updateAuthor
 * @updateDes
 */
public class CServiceFragment extends BaseFragment<CServicePresenter> {

    public static CServiceFragment instance;

    private Map<Integer, String> map = new HashMap<>();
    private DrawerLayout drawerLayout;
    private WebView webView;

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

    private static String TAG="CServiceFragment";

    public static CServiceFragment getInstance( DrawerLayout drawerLayout) {
        if (instance == null) {
            instance = new CServiceFragment(drawerLayout);
        }
        return instance;
    }

    public CServiceFragment(DrawerLayout drawerLayout) {
        this.drawerLayout=drawerLayout;
    }


    @Override
    protected void initData() {
        Log.d(TAG, "initData" );

    }

    @Override
    protected void initView() {

        webView=v.findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(Constants.CUSTOMER_SERVICE);
    }

    @Override
    protected int initLayoutID() {
        return R.layout.fragment_cs;
    }

    @Override
    protected CServicePresenter initPresenter() {
        return new CServicePresenter();
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
            drawerLayout.closeDrawers();
            for (int i = 0; i < fm.getBackStackEntryCount()-1; i++) {
                fm.popBackStack("CService", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.popBackStack("Personal", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            return;
        }
        System.exit(0);
    }
}
