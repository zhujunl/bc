package com.bc.sdk.model;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.bc.sdk.manager.DBManager;
import com.bc.sdk.model.bean.SignInfoBean;
import com.bc.sdk.model.utility.DeviceIdUtil;
import com.bc.sdk.db.entity.ConfigEntity;
import com.bc.sdk.view.Constants;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author WangKun
 * @description:
 * @date :2022/9/15 11:20
 */
public class RetrofitManager {
    public static RetrofitManager retrofitManager;
    private ApiService apiService;

    private RetrofitManager(Context context) {
        final X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new X509TrustManager[]{trustManager}, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }


        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.readTimeout(5000, TimeUnit.MILLISECONDS);
        client.connectTimeout(5000, TimeUnit.MILLISECONDS);
        if (Build.VERSION.SDK_INT < 29) {
            client.sslSocketFactory(sslContext.getSocketFactory());
        } else {
            client.sslSocketFactory(sslContext.getSocketFactory(), trustManager);
        }
        ;
        client.hostnameVerifier((hostname, session) -> true);
        client.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        client.addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request original = chain.request();
                SignInfoBean sign = new SignInfoBean();

                Request.Builder requestBuilder = original.newBuilder();
                try {
                    sign = DeviceIdUtil.getSign(context);
                    requestBuilder.header("sign", sign.sign);
                    requestBuilder.header("info", sign.info);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ConfigEntity authorization = DBManager.getInstance(context).getAuthorization();
                if (authorization != null && !TextUtils.isEmpty(authorization.getAuthorization())) {
                    requestBuilder.addHeader("Authorization", "Bearer " + authorization.getAuthorization());
                }
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        OkHttpClient build = client.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(build)
                .build();
        //关联
        apiService = retrofit.create(ApiService.class);
    }

    //单例
    public static RetrofitManager getInstance(Context context) {
        if (retrofitManager == null) {
            synchronized (RetrofitManager.class) {
                retrofitManager = new RetrofitManager(context);
            }
        }
        return retrofitManager;
    }

    public ApiService getApiService() {
        return apiService;
    }
}
