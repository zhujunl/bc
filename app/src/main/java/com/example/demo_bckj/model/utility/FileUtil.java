package com.example.demo_bckj.model.utility;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class FileUtil {

    static final String TAG="FileUtil";
    static final String path=Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"DCIM"+File.separator+"bc";

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

    /**/
    public static void saveImg(Context context,Bitmap bitmap){
        File file=new File(path+File.separator+ SystemClock.currentThreadTimeMillis()+".png");
        try {
            if (!file.exists()){
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            Log.d(TAG, "saveImg"  );
            fos.close();
            if (Build.VERSION.SDK_INT <29) {
                // 保存图片后发送广播通知更新数据库
                Uri uri = Uri.fromFile(file);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            }else {
                ContentResolver localContentResolver = context.getContentResolver();
                ContentValues localContentValues = getImageContentValues(context, file, System.currentTimeMillis());
                localContentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, localContentValues);

                Intent localIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                final Uri localUri = Uri.fromFile(file);
                localIntent.setData(localUri);
                context.sendBroadcast(localIntent);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static ContentValues getImageContentValues(Context paramContext, File paramFile, long paramLong) {
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("title", paramFile.getName());
        localContentValues.put("_display_name", paramFile.getName());
        localContentValues.put("mime_type", "image/jpeg");
        localContentValues.put("datetaken", Long.valueOf(paramLong));
        localContentValues.put("date_modified", Long.valueOf(paramLong));
        localContentValues.put("date_added", Long.valueOf(paramLong));
        localContentValues.put("orientation", Integer.valueOf(0));
        localContentValues.put("_data", paramFile.getAbsolutePath());
        localContentValues.put("_size", Long.valueOf(paramFile.length()));
        return localContentValues;
    }

}
