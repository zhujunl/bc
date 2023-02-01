package com.example.demo_bckj.model.utility.device;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.example.demo_bckj.model.utility.DeviceIdUtil;

import androidx.annotation.NonNull;

/**
 * @author ZJL
 * @date 2023/1/16 10:39
 * @des
 * @updateAuthor
 * @updateDes
 */

//"\"device\": {\n" +
//"        \"os\": \"Android\",\n" +
//"        \"android\": {\n" +
//"            \"system_version\": \"" + DeviceIdUtil.getSystemVersion() + "\",\n" +
//"            \"android_id\": \"" + DeviceIdUtil.getAndroidId(context) + "\",\n" +
//"            \"android_q\": {\n" +
//"                \"aaid\": \"" + "748d89ad-dd0d-424a-83df-d934519f0489" + "\",\n" +
//"                \"oaid\": \"" + "92e60edb3de3f9d0" + "\",\n" +
//"                \"vaid\": \"" + "416bffbb34374591" + "\"\n" +
//"            },\n" +
//"            \"id\": \"" + "QKQ1.200419.002" + "\",\n" +
//"            \"imei\": [" + DeviceIdUtil.getIMEI_1(context) + "," + DeviceIdUtil.getIMEI_2(context) + "],\n" +
//"            \"imsi\": \"" + DeviceIdUtil.getIMSI(context) + "\",\n" +
//"            \"model\": \"" + Build.MODEL + "\",\n" +
//"            \"product\": \"" + "cas" + "\",\n" +
//"            \"brand\": \"" + Build.BRAND + "\",\n" +
//"            \"game_package_name\": \"" + "" + "\",\n" +
//"            \"game_version\": \"" + "" + "\",\n" +
//"            \"sdk_package_name\": \"" + DeviceIdUtil.getTopPackage(context) + "\",\n" +
//"            \"sdk_version\": \"" + DeviceIdUtil.getVersionName(context) + "\",\n" +
//"            \"serial\": \"" + DeviceIdUtil.getSERIAL() + "\",\n" +
//"            \"sim_serial\": [" + DeviceIdUtil.getSimSerial(context) + "]\n" +
//"        },\n" +
//"        \"network\": {\n" +
//"            \"code\": 46002,\n" +
//"            \"intranet_ip\": \"10.48.6.16\",\n" +
//"            \"mac\": \"e0:1f:88:33:01:f0\",\n" +
//"            \"name\": \"那就这样\",\n" +
//"            \"type\": \"wifi\"\n" +
//"        }\n" +
//"    }\n"

public class Device {
    private String os;
    private DeviceAndroid android;
    private Network netWork;

    public Device(Context context) {
        this.os = "Android";
        this.android = new DeviceAndroid(context);
        this.netWork = new Network();
        netWork.setType(DeviceIdUtil.getNetworkType(context));
        netWork.setMac(DeviceIdUtil.getMacAddr());
        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        netWork.setCode(String.valueOf(telManager.getSimOperator()));
        String operator = DeviceIdUtil.getSimOperator(telManager.getSimOperator());
        netWork.setName(operator);
    }

    public Device(String os, DeviceAndroid android, Network netWork) {
        this.os = os;
        this.android = android;
        this.netWork = netWork;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public DeviceAndroid getAndroid() {
        return android;
    }

    public void setAndroid(DeviceAndroid android) {
        this.android = android;
    }

    public Network getNetWork() {
        return netWork;
    }

    public void setNetWork(Network netWork) {
        this.netWork = netWork;
    }

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "\"os\": \"" + os + '\"' +
                ",\"android\": " + android +
                ",\"network\": " + netWork +
                '}';
    }
}
