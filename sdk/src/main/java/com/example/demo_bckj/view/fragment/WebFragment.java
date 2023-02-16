package com.example.demo_bckj.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.example.demo_bckj.R;
import com.example.demo_bckj.listener.ClickListener;
import com.example.demo_bckj.view.Constants;
import com.example.demo_bckj.view.round.RoundView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * @author ZJL
 * @date 2023/2/7 10:37
 * @des
 * @updateAuthor
 * @updateDes
 */
public class WebFragment extends Fragment {
    private WebView webView;
    private Button btn;
    private TextView tittle;
    private ClickListener click;
    private FragmentManager fm;
    private boolean isDialog;
    private String  style;
    private Dialog dialog;

    public WebFragment(ClickListener click, Dialog dialog, FragmentManager fm, boolean isDialog, String style) {
        this.click=click;
        this.dialog=dialog;
        this.fm=fm;
        this.isDialog=isDialog;
        this.style=style;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        requireActivity().getOnBackPressedDispatcher().addCallback(this,new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.view_web, container,false);
        webView=inflate.findViewById(R.id.webview);
        btn=inflate.findViewById(R.id.back);
        tittle=inflate.findViewById(R.id.tittle);
        show(style);
        btn.setOnClickListener(v->{
            finish();
        });
        return inflate;
    }

    public void show(String  style){
        String uri= Constants.REGISTER;
        switch (style) {
            case "user":
                uri= Constants.REGISTER;
                tittle.setText("无限游戏用户协议");
                break;
            case "privacy":
                uri= Constants.PRIVACY;
                tittle.setText("无限游戏用户隐私政策");
                break;
            case "customer":
                uri= Constants.CUSTOMER_SERVICE;
                tittle.setText("客服");
                break;
        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(uri);
    }

    public void finish(){
//        Fragment home = fm.findFragmentByTag("homeFragment");
//        FragmentTransaction ft = fm.beginTransaction();
//        if (home==null){
//            home=HomeFragment.getInstance();
//            ft.add(R.id.home,home,"homeFragment");
//        }
        hide();
//        ft.show(home);
//        ft.commit();
        if (!isDialog){
            RoundView.getInstance().showSmallwin(getActivity(), click, 0);
        }else {
            dialog.show();
        }
    }

    public void hide(){
        FragmentTransaction transaction = fm.beginTransaction();
//        transaction.hide(this);
        fm.popBackStack();
        transaction.commit();
    }
}
