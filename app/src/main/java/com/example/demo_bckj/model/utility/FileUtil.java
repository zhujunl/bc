package com.example.demo_bckj.model.utility;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class FileUtil {

    /**
     *
     * @param context
     * @param defType
     * @param file_name
     * @return
     */

    public static int getResIdFromFileName(Context context, String defType, String file_name) {
        Resources rs = context.getResources();
        String packageName = getMyPackageName(context);
        return rs.getIdentifier(file_name, defType, packageName);
    }

    /**
     *
     * @param context
     * @return
     */

    public static String getMyPackageName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(),0).packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getResponseBody(ResponseBody responseBody) {

        Charset UTF8 = Charset.forName("UTF-8");
        BufferedSource source = responseBody.source();
        try {
            source.request(Long.MAX_VALUE); // Buffer the entire body.
        } catch (IOException e) {
            e.printStackTrace();
        }
        Buffer buffer = source.buffer();

        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            try {
                charset = contentType.charset(UTF8);
            } catch (UnsupportedCharsetException e) {
                e.printStackTrace();
            }
        }
        return JSONObject.parseObject(buffer.clone().readString(charset));
    }

}
