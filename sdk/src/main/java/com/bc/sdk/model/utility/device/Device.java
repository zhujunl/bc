package com.bc.sdk.model.utility.device;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.bc.sdk.model.utility.DeviceIdUtil;

import androidx.annotation.NonNull;

/**
 * @author ZJL
 * @date 2023/1/16 10:39
 * @des
 * @updateAuthor
 * @updateDes
 */

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
