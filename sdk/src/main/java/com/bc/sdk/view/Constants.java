package com.bc.sdk.view;

import android.Manifest;
import android.content.Context;

import com.bc.sdk.model.utility.ToastUtil;
import com.bc.sdk.model.utility.device.DeviceInfo;
import com.bc.sdk.BuildConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @author WangKun
 * @description:
 * @date :2022/9/15 11:10
 */
public class Constants {
    //    public static String BASE_URL="http://bcapi.mfbmn.com/";
    public static String BASE_URL = BuildConfig.baseUrl;
    public static String REGISTER = "https://static.infinite-game.cn/resources/protocol/register.html";
    public static String PRIVACY = "https://static.infinite-game.cn/resources/protocol/register.html";
    public static String DEVICE = "https://static.infinite-game.cn/resources/protocol/register.html";
    public static String CUSTOMER_SERVICE = "https://sdk.infinite-game.cn/contact?game=f60e429f71e147eb817f233f9fca4cce";


    public static String[] PermissionString = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    public static String TYPE = "ML";
    public static String GAME = "f60e429f71e147eb817f233f9fca4cce";
    public static String CHANNEL = "7cd0b27069248f109480e7358e058a78";
    public static String PLAN = "a5cfce740f5f48168bb08140ba2dfdd4";
    public static String PACKAGE = "65767249bd2a4fb2a4e195fe59aa54aa";
    public static Map<String, String> MAP;

    static {
        MAP = new HashMap<String, String>();
        MAP.put("type", TYPE);
        MAP.put("game", GAME);
    }

    public static DeviceInfo DEVICEINFO;

    private static long lastClickTime=0L;

    public static boolean isFastDoubleClick(Context context) {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 500) {
            lastClickTime = time;
            ToastUtil.show(context,"请勿重复点击");
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
