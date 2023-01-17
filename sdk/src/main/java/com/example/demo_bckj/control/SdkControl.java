package com.example.demo_bckj.control;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.demo_bckj.manager.HttpManager;
import com.example.demo_bckj.model.bean.RechargeOrder;
import com.example.demo_bckj.model.bean.RoleBean;
import com.example.demo_bckj.model.utility.FileUtil;
import com.example.demo_bckj.model.utility.device.Device;
import com.example.demo_bckj.model.utility.device.DeviceInfo;
import com.example.demo_bckj.view.Constants;
import com.example.demo_bckj.view.dialog.RechargeSubDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

/**
 * @author ZJL
 * @date 2023/1/12 17:10
 * @des
 * @updateAuthor
 * @updateDes
 */
public class SdkControl {

    private static SdkControl instance;
    private Context context;

    public static SdkControl getInstance(Context context) {
        if (instance == null) {
            instance = new SdkControl(context);
        }
        return instance;
    }

    public SdkControl(Context context) {
        this.context = context;
    }

    public void init(Map<String, String> gameConfig) {
        boolean fileExists = FileUtil.isFileExists(context, "bc_sdk_config.json");
        if (!fileExists) {
            if (gameConfig != null) {
                if (!hasGame(gameConfig)) {
                    gameConfig.put("game", Constants.GAME);
                }
                Constants.DEVICEINFO = new DeviceInfo(gameConfig, new Device(context));
            } else {
                Constants.DEVICEINFO = new DeviceInfo(Constants.MAP, new Device(context));
            }
        } else {
            Constants.DEVICEINFO = new DeviceInfo(FileUtil.getMap(context, "bc_sdk_config.json"), new Device(context));
        }
        try {
            JSONObject jsonObject = new JSONObject(Constants.DEVICEINFO.toString());
//            String value = getValue(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
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

    //用户创角
    public void CreateRole(Context context, RoleBean roleBean) {
        HttpManager.getInstance().CreateRole(context, roleBean);
    }

    //角色登录区服
    public void LoginServer(Context context, RoleBean roleBean) {
        HttpManager.getInstance().LoginServer(context, roleBean);
    }

    //充值订单
    public void Recharge(Activity context, SDKListener sdkListener, RechargeOrder rechargeOrder) {
        RechargeSubDialog rechargeSubDialog = new RechargeSubDialog(context, rechargeOrder, sdkListener);
        rechargeSubDialog.show();
    }

    public void Recharge(Activity context, SDKListener sdkListener, boolean exception) {
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
        RechargeSubDialog rechargeSubDialog = new RechargeSubDialog(context, rechargeOrder, sdkListener, exception);
        rechargeSubDialog.show();
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
                    Log.d("getValue", "String=="+sb.toString());
                } else {
                    JSONObject jsonObject = new JSONObject(o.toString());
                    String value = getValue(jsonObject);
                    sb.append("\"").append(next).append("\":").append(value).append(",");
                    Log.d("getValue", "Object=="+sb.toString());
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
