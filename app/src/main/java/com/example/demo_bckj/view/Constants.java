package com.example.demo_bckj.view;

import android.Manifest;

/**
 * @author WangKun
 * @description:
 * @date :2022/9/15 11:10
 */
public class Constants {
//    public static String BASE_URL="http://bcapi.mfbmn.com/";
    public static String BASE_URL="https://apitest.infinite-game.cn";
    public static String REGISTER="https://static.infinite-game.cn/resources/protocol/register.html";
    public static String PRIVACY="https://static.infinite-game.cn/resources/protocol/register.html";
    public static String DEVICE="https://static.infinite-game.cn/resources/protocol/register.html";

    public static String[] PermissionString = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
}
