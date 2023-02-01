package com.example.demo_bckj.broadcast;

/**
 * @author ZJL
 * @date 2023/2/1 10:38
 * @des
 * @updateAuthor
 * @updateDes
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.demo_bckj.model.utility.DeviceIdUtil;
import com.example.demo_bckj.model.utility.device.Network;
import com.example.demo_bckj.view.Constants;

public class NetworkConnectChangedReceiver extends BroadcastReceiver {
    private String TAG="NetworkConnectChangedReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiinfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(!networkInfo.isConnected()&&!wifiinfo.isConnected()){
            Log.e(TAG, "网络不可用!"  );
        }else{
            if(wifiinfo.isConnected()){
                Log.d(TAG, "wifi连接中!"  );
                Network netWork = Constants.DEVICEINFO.getDevice().getNetWork();
                netWork.setIntranet_ip(DeviceIdUtil.getNetworkIp());
                netWork.setType(DeviceIdUtil.getNetworkType(context));
                netWork.setMac(DeviceIdUtil.getMacAddr());
                TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                netWork.setCode(String.valueOf(telManager.getSimOperator()));
                String operator = DeviceIdUtil.getSimOperator(telManager.getSimOperator());
                netWork.setName(operator);
            }
            if(networkInfo.isConnected()){
                Log.d(TAG, "流量连接中!" );
                Network netWork = Constants.DEVICEINFO.getDevice().getNetWork();
                netWork.setIntranet_ip(DeviceIdUtil.getNetworkIp());
                netWork.setType(DeviceIdUtil.getNetworkType(context));
                netWork.setMac(DeviceIdUtil.getMacAddr());
                TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                netWork.setCode(String.valueOf(telManager.getSimOperator()));
                String operator = DeviceIdUtil.getSimOperator(telManager.getSimOperator());
                netWork.setName(operator);
            }
        }

    }

}

