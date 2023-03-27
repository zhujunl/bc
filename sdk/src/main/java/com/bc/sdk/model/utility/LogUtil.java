package com.bc.sdk.model.utility;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ZJL
 * @date 2023/3/22 16:58
 * @des
 * @updateAuthor
 * @updateDes
 */
public class LogUtil {
    @SuppressLint("SimpleDateFormat")
    final static SimpleDateFormat logDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    final static String TAG = "LogUtil";
    public static boolean isDebug = false;
    public static boolean isSave = false;

    public static void v(String message) {
        if (isDebug) {
            Log.v(TAG, message);
        }
        if (isSave) {
            Date date = new Date(System.currentTimeMillis());
            String format = logDateFormat.format(date);
            StringBuffer sb = new StringBuffer();
            sb.append("\n").append(format).append("  v  ").append(message);
            FileUtil.saveLog(sb.toString());
        }
    }

    public static void d(String message) {
        if (isDebug) {
            Log.d(TAG, message);
        }
        if (isSave) {
            Date date = new Date(System.currentTimeMillis());
            String format = logDateFormat.format(date);
            StringBuffer sb = new StringBuffer();
            sb.append("\n").append(format).append("  d  ").append(message);
            FileUtil.saveLog(sb.toString());
        }
    }

    public static void i(String message) {
        if (isDebug) {
            Log.i(TAG, message);
        }
        if (isSave) {
            Date date = new Date(System.currentTimeMillis());
            String format = logDateFormat.format(date);
            StringBuffer sb = new StringBuffer();
            sb.append("\n").append(format).append("  i  ").append(message);
            FileUtil.saveLog(sb.toString());
        }
    }

    public static void w(String message) {
        if (isDebug) {
            Log.w(TAG, message);
        }
        if (isSave) {
            Date date = new Date(System.currentTimeMillis());
            String format = logDateFormat.format(date);
            StringBuffer sb = new StringBuffer();
            sb.append("\n").append(format).append("  w  ").append(message);
            FileUtil.saveLog(sb.toString());
        }
    }

    public static void e(String message) {
        if (isDebug) {
            Log.e(TAG, message);
        }
        if (isSave) {
            Date date = new Date(System.currentTimeMillis());
            String format = logDateFormat.format(date);
            StringBuffer sb = new StringBuffer();
            sb.append("\n").append(format).append("  e  ").append(message);
            FileUtil.saveLog(sb.toString());
        }
    }
}
