package com.bc.sdk.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.bc.sdk.R;
import com.bc.sdk.view.Constants;

import androidx.appcompat.app.AppCompatActivity;

public class AgreementActivity extends AppCompatActivity {

    private WebView webView;
    private Button btn;
    private TextView tittle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Intent intent = getIntent();
        String style = intent.getStringExtra("style");
        webView=findViewById(R.id.webview);
        btn=findViewById(R.id.back);
        tittle=findViewById(R.id.tittle);
        show(style);
        btn.setOnClickListener(v->{
            finish();
        });
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
}