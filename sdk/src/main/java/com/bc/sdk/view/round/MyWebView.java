package com.bc.sdk.view.round;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bc.sdk.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author ZJL
 * @date 2023/1/4 15:15
 * @des
 * @updateAuthor
 * @updateDes
 */
public class MyWebView extends FrameLayout {
    private WebView webView;
    private Button btn;
    private TextView tittle;



    public MyWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_web,this);
        webView=findViewById(R.id.webview);
        btn=findViewById(R.id.back);
        tittle=findViewById(R.id.tittle);
    }

    public void show(String uri){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(uri);
    }
    public void setTittle(String tittle){
        this.tittle.setText(tittle);
    }

    public void btnListener(OnClickListener listener){
        btn.setOnClickListener(listener);
    }

    public void setOnKey(OnKeyListener listener){
        webView.setOnKeyListener(listener);
    }

    public WebView getWebView() {
        return webView;
    }
}
