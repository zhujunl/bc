package com.bc.sdk.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bc.sdk.R;

import androidx.appcompat.app.AppCompatActivity;

public class WXPayActivity extends AppCompatActivity {
    String TAG="WXPayActivity";
    WebView webView;
    boolean isLoad=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay);
        webView = ((WebView) findViewById(R.id.wxWeb));
        Bundle extras = getIntent().getExtras();
        Object url = extras.get("url");
        Log.d(TAG, "url==" + url);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebChromeClient(new WebChromeClient());
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    finish();
                    return true;
                } catch (Exception e) {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(WXPayActivity.this,R.style.alertDialog);
                    builder.setOnDismissListener(dialog -> finish());
                    builder.setTitle("支付中心").
                            setMessage("该手机没有安装微信客户端，请安装微信后重新完成支付，或换用支付宝进行支付").
                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create().show();
                    return true;
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, android.net.http.SslError error) { // 重写此方法可以让webview处理https请求
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (!isLoad){
                    isLoad=true;
                    shouldOverrideUrlLoading(view,url);
                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);
            }
        };
        webView.setWebViewClient(webViewClient);
        webView.loadUrl(url.toString());
    }
}