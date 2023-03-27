package com.bc.sdk.model.utility;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import org.json.JSONException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class FileUtil {

    static final String TAG = "FileUtil";
    static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    static final String PIC = PATH + File.separator + "DCIM" + File.separator + "bc";
    static final String BC = PATH + File.separator + "bc";


    public static int getResIdFromFileName(Context context, String defType, String file_name) {
        Resources rs = context.getResources();
        String packageName = getMyPackageName(context);
        return rs.getIdentifier(file_name, defType, packageName);
    }

    /**
     * @param context
     * @return
     */

    public static String getMyPackageName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getResponseBody(ResponseBody responseBody) {

        Charset UTF8 = Charset.forName("UTF-8");
        if (responseBody.source() == null)
            return null;
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
    public static void saveImg(Context context, Bitmap bitmap) {
        File file = new File(PIC + File.separator + SystemClock.currentThreadTimeMillis() + ".png");
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            Log.d(TAG, "saveImg");
            fos.close();
            if (Build.VERSION.SDK_INT < 29) {
                // 保存图片后发送广播通知更新数据库
                Uri uri = Uri.fromFile(file);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            } else {
                ContentResolver localContentResolver = context.getContentResolver();
                ContentValues localContentValues = getImageContentValues(context, file, System.currentTimeMillis());
                localContentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, localContentValues);

                Intent localIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
                final Uri localUri = Uri.fromFile(file);
                localIntent.setData(localUri);
                context.sendBroadcast(localIntent);
            }
        } catch (Exception e) {
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


    /**
     * 获取Assets路径下的文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getJson(Context context, String fileName) {
        String json = null;
        AssetManager s = context.getAssets();
        try {
            InputStream is = null;
            try {
                is = s.open(fileName);
                byte[] buffer = new byte[is.available()];
                is.read(buffer);
                json = new String(buffer, "utf-8");
                is.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 获取Assets路径下的文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getMapString(Context context, String fileName) {
        String json = null;
        AssetManager s = context.getAssets();
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = null;
            is = s.open(fileName);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            json = new String(buffer, "utf-8");
            org.json.JSONObject jsonObject = new org.json.JSONObject(json);
            Iterator<String> iterator = jsonObject.keys();
            Map<String, String> map = new HashMap<>();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = jsonObject.getString(key);
                map.put(key, value);
                sb.append(key).append(":").append(value).append(",\n");
            }
            is.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static Map<String, String> getMap(Context context, String fileName) {
        String json = null;
        AssetManager s = context.getAssets();
        Map<String, String> map = new HashMap<>();
        try {
            InputStream is = null;
            is = s.open(fileName);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            json = new String(buffer, "utf-8");
            JSONObject jsonObject = JSONObject.parseObject(json);
            Iterator<String> iterator = jsonObject.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = jsonObject.getString(key);
                map.put(key, value);
            }
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
     * 判断assets文件夹下的文件是否存在
     *
     * @return false 不存在    true 存在
     */
    public static boolean isFileExists(Context context, String filename) {
        AssetManager assetManager = context.getAssets();
        try {
            String[] names = assetManager.list("");
            for (int i = 0; i < names.length; i++) {
                if (names[i].equals(filename.trim())) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 日志保存
     */
    public static void saveLog(String message) {
        String FILENAME = "log";
        File file = new File(BC + File.separator + FILENAME);
        FileOutputStream fos = null;
        BufferedWriter writer = null;
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            fos = new FileOutputStream(file, true);
            writer = new BufferedWriter(new OutputStreamWriter(fos));
            writer.write(message);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
