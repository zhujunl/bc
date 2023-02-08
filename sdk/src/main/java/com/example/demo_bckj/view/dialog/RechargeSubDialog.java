package com.example.demo_bckj.view.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.demo_bckj.R;
import com.example.demo_bckj.control.RechargeCallBack;
import com.example.demo_bckj.manager.HttpManager;
import com.example.demo_bckj.model.bean.RechargeOrder;
import com.example.demo_bckj.model.utility.StrUtil;
import com.example.demo_bckj.view.adapter.RechargeAdapter;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author ZJL
 * @date 2022/12/16 11:03
 * @des 支付选择弹窗
 * @updateAuthor
 * @updateDes
 */
public class RechargeSubDialog extends Dialog {

    private final String TAG = "RechargeSubDialog";

    private Activity context;
    private TextView commodity, comDetails;
    private RecyclerView rv;
    private RechargeAdapter adapter;
    private Button btn;
    private List<String> lists = Arrays.asList("微信", "支付宝");
    private String number_game = "游戏订单号", props_name = "64 位以内字符串", server_id = "32 位以内字符串", server_name = "32 位以内字符串",
            role_id = "32 位以内字符串", role_name = "32 位以内字符串", callback_url = "https://apitest.infinite-game.cn/ping", extend_data = "";
    private int money = 1;
    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private int pos;
    private RechargeOrder rechargeOrder;
    private String orderNum;
    private RechargeCallBack listener;

    public RechargeSubDialog(@NonNull Activity context, RechargeOrder rechargeOrder, RechargeCallBack listener) {
        super(context);
        setContentView(R.layout.dialog_recharge_sub);
        this.context = context;
        this.listener = listener;
        this.rechargeOrder = rechargeOrder;
        Log.d(TAG, "订单信息==" + rechargeOrder.toString());

        commodity = findViewById(R.id.Commodity);
        comDetails = findViewById(R.id.ComDetails);
        rv = findViewById(R.id.recharge_rv);
        btn = findViewById(R.id.popup_submit);
        adapter = new RechargeAdapter(context, lists, position -> {
            pos = position;
        });
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        rv.setAdapter(adapter);
        commodity.setText(rechargeOrder.getProps_name());
        comDetails.setText("￥" + StrUtil.changeF2Y(String.valueOf(rechargeOrder.getMoney())));
        btn.setOnClickListener(v -> {
            new Thread(() -> {
                if (pos == 1) {
                    HttpManager.getInstance().charge(context,rechargeOrder,listener);
                } else {
                    wxCharge();
                }
            }).start();
            dismiss();
        });
    }

    //微信支付
    private void wxCharge() {
        //        try {
        //            Response<ResponseBody> execute = RetrofitManager.getInstance(getContext()).getApiService().CreateOrder(number_game, money, props_name, server_id,
        //                    server_name, role_id, role_name, callback_url, extend_data).execute();
        //            JSONObject json = FileUtil.getResponseBody(execute.body());
        //            if (json.get("code").toString().equals("0")){
        //                OrderBean response = JSONObject.toJavaObject(json, OrderBean.class);
        //                String number = response.getData().getNumber();
        //                Response<ResponseBody> aliExecute = RetrofitManager.getInstance(getContext()).getApiService().WeiChatPay(number).execute();
        //                JSONObject aliJson = FileUtil.getResponseBody(aliExecute.body());
        //                if (aliJson.get("code").toString().equals("0")){
        //                    WXPayBean pay = JSONObject.toJavaObject(aliJson, WXPayBean.class);
        ////                    Intent intent=new Intent(ActivityManager.getInstance().getCurrentActivity(),WebActivity.class);
        ////                    intent.putExtra("url",pay.getData().getCode());
        ////                    context.startActivity(intent);
        //                }else {
        //                    Toast.makeText(context, aliJson.get("message").toString(), Toast.LENGTH_SHORT).show();
        //                }
        //            }else {
        //                Toast.makeText(context, json.get("message").toString(), Toast.LENGTH_SHORT).show();
        //            };
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }
    }


    @SuppressLint("WrongConstant")
    public static void toWeChatScanDirect(Context context) {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI"));
            intent.putExtra("LauncherUI.From.Scaner.Shortcut", true);
            intent.setFlags(335544320);
            intent.setAction("android.intent.action.VIEW");
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }

}
