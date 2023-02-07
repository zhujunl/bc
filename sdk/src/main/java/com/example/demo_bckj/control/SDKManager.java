package com.example.demo_bckj.control;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.demo_bckj.manager.HttpManager;
import com.example.demo_bckj.model.MyCallback;
import com.example.demo_bckj.model.bean.RechargeOrder;
import com.example.demo_bckj.model.bean.RoleBean;
import com.example.demo_bckj.model.utility.FileUtil;
import com.example.demo_bckj.model.utility.device.Device;
import com.example.demo_bckj.model.utility.device.DeviceInfo;
import com.example.demo_bckj.view.Constants;
import com.example.demo_bckj.view.dialog.RechargeSubDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.ResponseBody;

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
     * @param context    上下文
     * @param gameId 游戏配置信息，可以为空。
     *
     */
    public void init(Activity context, String gameId) {
        this.context = context;
        Map<String, String> map = new HashMap<>();
        map.put("game", gameId); // 游戏唯一标识
        boolean fileExists = FileUtil.isFileExists(context, "bc_sdk_config.json");
        if (!fileExists) {
            if (map != null&& !TextUtils.isEmpty(gameId)) {
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
    public void CreateRole(Context context, RoleBean roleBean, MyCallback<ResponseBody> callback) {
        HttpManager.getInstance().CreateRole(context, roleBean, callback);
    }

    /**
     * 角色登录区服
     *
     * @param context  上下文
     * @param roleBean 角色类
     * @param callback 回调
     */
    public void LoginServer(Context context, RoleBean roleBean, MyCallback<ResponseBody> callback) {
        HttpManager.getInstance().LoginServer(context, roleBean, callback);
    }

    /**
     * 创建订单
     *
     * @param context          上下文
     * @param rechargeOrder    订单实体类
     * @param rechargeListener 订单支付回调监听
     */
    public void Recharge(Activity context, RechargeOrder rechargeOrder, RechargeListener rechargeListener) {
        RechargeSubDialog rechargeSubDialog = new RechargeSubDialog(context, rechargeOrder, rechargeListener);
        rechargeSubDialog.show();
    }

    public void Recharge(Activity context, boolean exception, RechargeListener listener) {
        RechargeOrder rechargeOrder = new RechargeOrder.Builder()
                .number_game("游戏订单号")
                .props_name("物品名称")
                .server_id("区服 ID")
                .server_name("区服名称")
                .role_id("角色 ID")
                .role_name("角色名称")
                .callback_url("https://apitest.infinite-game.cn/ping")
                .money(1)
                .extend_data("")
                .build();
        RechargeSubDialog rechargeSubDialog = new RechargeSubDialog(context, rechargeOrder, listener, exception);
        rechargeSubDialog.show();
    }

    /**
     * 游戏主动登录
     *
     * @param context       上下文
     * @param loginListener 登录回调监听
     */
    public void Login(Context context, LoginListener loginListener) {
        HttpManager.getInstance().Login(context, loginListener);
    }

    /**
     * 游戏主动退出登录
     *
     * @param isDestroy        退出登录是否退出应用
     * @param isLoginShow      退出登录是否弹出登录
     * @param loginOutListener 退出回调监听
     */
    public void LoginOut(boolean isDestroy, boolean isLoginShow, LoginOutListener loginOutListener) {
        HttpManager.getInstance().loginOut(context, isDestroy, isLoginShow, loginOutListener);
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
