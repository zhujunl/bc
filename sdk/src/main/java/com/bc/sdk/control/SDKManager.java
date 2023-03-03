package com.bc.sdk.control;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.WindowManager;

import com.bc.sdk.broadcast.NetworkConnectChangedReceiver;
import com.bc.sdk.manager.DialogManager;
import com.bc.sdk.manager.HttpManager;
import com.bc.sdk.model.bean.RechargeOrder;
import com.bc.sdk.model.bean.RoleBean;
import com.bc.sdk.model.utility.Base64;
import com.bc.sdk.model.utility.FileUtil;
import com.bc.sdk.model.utility.device.Device;
import com.bc.sdk.model.utility.device.DeviceInfo;
import com.bc.sdk.view.Constants;
import com.bc.sdk.view.dialog.RechargeSubDialog;
import com.meituan.android.walle.ChannelInfo;
import com.meituan.android.walle.WalleChannelReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author ZJL
 * @date 2023/1/12 17:10
 * @des
 * @updateAuthor
 * @updateDes
 */
public class SDKManager {

    private static SDKManager instance;
    private Context context;
    private Activity activity;

    private NetworkConnectChangedReceiver networkChange;

    public static SDKManager getInstance() {
        if (instance == null) {
            instance = new SDKManager();
        }
        return instance;
    }

    public SDKManager() {
    }

    /**
     * SDK初始化接口
     *
     * @param activity         调用初始化接口的ACtivity
     * @param gameId           游戏配置信息，可以为空。
     * @param loginCallBack    登录回调
     * @param loginOutCallBack 退出回调
     */
    public void init(Activity activity, String gameId, LoginCallBack loginCallBack, LoginOutCallBack loginOutCallBack) {
        this.context = activity;
        this.activity = activity;
        Map<String, String> map = new HashMap<>();
        map.put("game", gameId); // 游戏唯一标识
        map.put("type", Constants.TYPE);
        Map<String, String> channel = getChannel(activity);
        boolean fileExists = FileUtil.isFileExists(context, "bc_sdk_config.json");
        if (!fileExists) {
            if (channel==null){
                if (!TextUtils.isEmpty(gameId)) {
                    Constants.DEVICEINFO = new DeviceInfo(map, new Device(context));
                } else {
                    Constants.DEVICEINFO = new DeviceInfo(Constants.MAP, new Device(context));
                }
            }else {
                Constants.DEVICEINFO = new DeviceInfo(channel, new Device(context));
            }
        } else {
            Constants.DEVICEINFO = new DeviceInfo(FileUtil.getMap(context, "bc_sdk_config.json"), new Device(context));
        }
        HttpManager.getInstance().init(loginCallBack, loginOutCallBack);
        HttpManager.getInstance().setRecharge();
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        DialogManager.getInstance().init(activity);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        networkChange = new NetworkConnectChangedReceiver();
        activity.registerReceiver(networkChange, filter);
    }

    public void cancellation() {
        DialogManager.getInstance().cancellation();
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        activity.unregisterReceiver(networkChange);
    }

    private Map<String, String> getChannel(Context context) {
        ChannelInfo channelInfo = WalleChannelReader.getChannelInfo(context);
        if (channelInfo == null) {
            return null;
        }
        Map<String, String> extraInfo = channelInfo.getExtraInfo();
        String sdk_config = extraInfo.get("sdk_config");
        String decode = new String(Base64.decode(sdk_config));
        try {
            JSONObject jsonObject = new JSONObject(decode);
            Iterator<String> iterator = jsonObject.keys();
            Map<String, String> map = new HashMap<>();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = jsonObject.getString(key);
                map.put(key, value);
            }
            return map;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 用户创角
     *
     * @param context  上下文
     * @param roleBean 角色类
     * @param callback 回调
     */
    public void CreateRole(Context context, RoleBean roleBean, GameCallBack callback) {
        HttpManager.getInstance().CreateRole(context, roleBean, callback);
    }

    /**
     * 角色登录区服
     *
     * @param context  上下文
     * @param roleBean 角色类
     * @param callback 回调
     */
    public void LoginServer(Context context, RoleBean roleBean, GameCallBack callback) {
        HttpManager.getInstance().LoginServer(context, roleBean, callback);
    }

    /**
     * 创建订单
     *
     * @param activity      调用支付订单的Activity
     * @param rechargeOrder 订单实体类
     * @param callBack      订单支付回调监听
     */
    public void Recharge(Activity activity, RechargeOrder rechargeOrder, RechargeCallBack callBack) {
        RechargeSubDialog rechargeSubDialog = new RechargeSubDialog(activity, rechargeOrder, callBack);
        rechargeSubDialog.show();
    }

    /**
     * 游戏主动登录
     *
     * @param context  上下文
     * @param callBack 登录回调监听
     */
    public void Login(Context context, LoginCallBack callBack) {
        HttpManager.getInstance().Login(context, callBack);
    }

    /**
     * 游戏主动退出登录
     *
     * @param isDestroy   退出登录是否退出应用
     * @param isLoginShow 退出登录是否弹出登录
     * @param callBack    退出回调监听
     */
    public void LoginOut(boolean isDestroy, boolean isLoginShow, LoginOutCallBack callBack) {
        HttpManager.getInstance().loginOut(context, isDestroy, isLoginShow, callBack);
    }

}
