package com.example.demo_bckj.control;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.demo_bckj.R;
import com.example.demo_bckj.manager.HttpManager;
import com.example.demo_bckj.model.bean.RechargeOrder;
import com.example.demo_bckj.model.bean.RoleBean;
import com.example.demo_bckj.model.utility.FileUtil;
import com.example.demo_bckj.model.utility.device.Device;
import com.example.demo_bckj.model.utility.device.DeviceInfo;
import com.example.demo_bckj.view.Constants;
import com.example.demo_bckj.view.dialog.RechargeSubDialog;
import com.example.demo_bckj.view.fragment.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

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
        Map<String, String> map = new HashMap<>();
        map.put("game", gameId); // 游戏唯一标识
        boolean fileExists = FileUtil.isFileExists(context, "bc_sdk_config.json");
        if (!fileExists) {
            if (map != null && !TextUtils.isEmpty(gameId)) {
                if (!hasGame(map)) {
                    map.put("game", Constants.GAME);
                }
                Constants.DEVICEINFO = new DeviceInfo(map, new Device(context));
            } else {
                Constants.DEVICEINFO = new DeviceInfo(Constants.MAP, new Device(context));
            }
        } else {
            Constants.DEVICEINFO = new DeviceInfo(FileUtil.getMap(context, "bc_sdk_config.json"), new Device(context));
        }
        HttpManager.getInstance().init(loginCallBack, loginOutCallBack);
        HttpManager.getInstance().setRecharge();

        HomeFragment homeFragment = HomeFragment.getInstance();
        FragmentManager fm = ((FragmentActivity) activity).getSupportFragmentManager();
        homeFragment.setFm(fm);
        fm.beginTransaction()
                .add(R.id.home,homeFragment,"homeFragment")
                .commit();
    }

    private boolean hasGame(Map<String, String> map) {
        boolean flag = false;
        Iterator<String> iterator = map.keySet().iterator();
        do {
            String next = iterator.next();
            if (next.equals("game")) {
                flag = true;
                break;
            }
        } while (iterator.hasNext());
        return flag;
    }

    private boolean hasType(Map<String, String> map) {
        boolean flag = false;
        Iterator<String> iterator = map.keySet().iterator();
        do {
            String next = iterator.next();
            if (next.equals("type")) {
                flag = true;
                break;
            }
        } while (iterator.hasNext());
        return flag;
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

    private String getValue(JSONObject json) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        try {
            Iterator<String> iterator = json.keys();
            while (iterator.hasNext()) {
                String next = iterator.next();
                Object o = null;
                o = json.get(next);
                if (o instanceof String) {
                    sb.append("\"").append(next).append("\":\"").append(o.toString()).append("\",");
                    Log.d("getValue", "String==" + sb.toString());
                } else {
                    JSONObject jsonObject = new JSONObject(o.toString());
                    String value = getValue(jsonObject);
                    sb.append("\"").append(next).append("\":").append(value).append(",");
                    Log.d("getValue", "Object==" + sb.toString());
                }
            }
            Log.d("getValue", "sb=" + sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sb.append("}");
        return sb.toString();
    }

}
