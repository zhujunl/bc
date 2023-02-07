package com.example.demo_bckj.view.fragment;

import android.os.Bundle;
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
    private int style;

    public WebFragment(ClickListener click, FragmentManager fm, boolean isDialog, int style) {
        this.click=click;
        this.fm=fm;
        this.isDialog=isDialog;
        this.style=style;
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
            fm.popBackStack("webFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        });
        return inflate;
    }

    public void show(int style){
        String uri= Constants.REGISTER;
        switch (style) {
            case 0:
                uri= Constants.REGISTER;
                tittle.setText("无限游戏用户协议");
                break;
            case 1:
                uri= Constants.PRIVACY;
                tittle.setText("无限游戏用户隐私政策");
                break;
            case 2:
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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("WebFragment", "onDestroyView"  );
        if (!isDialog){
            RoundView.getInstance().showSmallwin(getActivity(), click, 0);
        }
    }
}
