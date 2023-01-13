package com.example.demo_bckj.control;

import android.content.Context;

import com.example.demo_bckj.manager.HttpManager;
import com.example.demo_bckj.model.bean.RechargeOrder;
import com.example.demo_bckj.model.bean.RoleBean;
import com.example.demo_bckj.view.dialog.RechargeSubDialog;

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
    private String type = "", game = "", channel = "", plan = "", pack = "";

    public static SdkControl getInstance(Context context) {
        if (instance == null) {
            instance = new SdkControl(context);
        }
        return instance;
    }

    public SdkControl(Context context) {
        this.context = context;
    }

    public void init(String type, String game, String channel, String plan, String pack) {
        this.type = type;
        this.game = game;
        this.channel = channel;
        this.plan = plan;
        this.pack = pack;
    }

    public String  getValue(String key){
        String value="";
        switch (key){
            case "type":
                value=type;
                break;
            case "game":
                value=game;
                break;
            case "channel":
                value=channel;
                break;
            case "plan":
                value=plan;
                break;
            case "pack":
                value=pack;
                break;
            default:
                break;
        }
        return value;
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
    public void Recharge(Context context, SDKListener sdkListener,RechargeOrder rechargeOrder) {
        RechargeSubDialog rechargeSubDialog = new RechargeSubDialog(context, rechargeOrder, sdkListener);
        rechargeSubDialog.show();
    }

    public void Recharge(Context context, SDKListener sdkListener, boolean exception) {
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
}
