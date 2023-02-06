package com.example.demo_bckj.control;

import android.app.Activity;
import android.content.Context;
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
    /**
     * @param gameConfig 游戏配置信息，可以为空。
     *                   存在bc_sdk_config.json时使用文件中数据，不存在时使用gameConfig数据，gameConfig为空则使用默认数据
     * */
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
    public void CreateRole(Context context, RoleBean roleBean, MyCallback<ResponseBody> callback) {
        HttpManager.getInstance().CreateRole(context, roleBean,callback);
    }

    /**
     *角色登录区服
     * @param context 上下文
     * @param roleBean 角色类
     * @param callback 回调
     * */
    public void LoginServer(Context context, RoleBean roleBean,MyCallback<ResponseBody> callback) {
        HttpManager.getInstance().LoginServer(context, roleBean,callback);
    }

    /**
     * 创建订单
     * @param context 上下文
     * @param sdkListener sdk监听接口
     * @param rechargeOrder 订单实体类
     * */
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

    /**登录
     * @param context 上下文
     * */
    public void Login(Context context){
        HttpManager.getInstance().Login(context);
    }

    /**退出登录
     * */
    public void LoginOut(){
        HttpManager.getInstance().loginOut(context, false, false);
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
