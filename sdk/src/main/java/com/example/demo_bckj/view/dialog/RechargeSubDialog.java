package com.example.demo_bckj.view.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.example.demo_bckj.R;
import com.example.demo_bckj.control.RechargeListener;
import com.example.demo_bckj.model.RetrofitManager;
import com.example.demo_bckj.model.bean.AliPayBean;
import com.example.demo_bckj.model.bean.OrderBean;
import com.example.demo_bckj.model.bean.PayResult;
import com.example.demo_bckj.model.bean.RechargeOrder;
import com.example.demo_bckj.model.utility.FileUtil;
import com.example.demo_bckj.model.utility.StrUtil;
import com.example.demo_bckj.view.adapter.RechargeAdapter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Response;

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
    private RechargeListener listener;

    public RechargeSubDialog(@NonNull Activity context, RechargeOrder rechargeOrder, RechargeListener listener) {
        super(context);
        setContentView(R.layout.dialog_recharge_sub);
        this.context = context;
        this.listener = listener;
        mHandlerThread = new HandlerThread("payOrder");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                        /**
                         * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                         */
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                        String resultStatus = payResult.getResultStatus();
                        // 判断resultStatus 为9000则代表支付成功
                        if (TextUtils.equals(resultStatus, "9000")) {
                            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                            Toast.makeText(context, context.getString(R.string.pay_success), Toast.LENGTH_SHORT).show();
                            listener.onSuccess(context.getString(R.string.orderNum) + orderNum);
                        } else {
                            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                            Toast.makeText(context, context.getString(R.string.pay_failed) + payResult.getMemo(), Toast.LENGTH_SHORT).show();
                            listener.onFail(context.getString(R.string.orderNum) + orderNum + context.getString(R.string.pay_failed) + payResult.getMemo());
                        }
                        break;
                    default:
                        break;
                }
            }
        };
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
                    charge();
                } else {
                    wxCharge();
                }
            }).start();
            dismiss();
        });
    }

    public RechargeSubDialog(@NonNull Activity context, RechargeOrder rechargeOrder, RechargeListener listener, boolean exception) {
        super(context);
        setContentView(R.layout.dialog_recharge_sub);
        this.context = context;
        mHandlerThread = new HandlerThread("payOrder");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                        /**
                         * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                         */
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                        String resultStatus = payResult.getResultStatus();
                        // 判断resultStatus 为9000则代表支付成功
                        if (TextUtils.equals(resultStatus, "9000")) {
                            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                            Toast.makeText(context, context.getString(R.string.pay_success), Toast.LENGTH_SHORT).show();
                            listener.onSuccess(context.getString(R.string.orderNum) + orderNum);
                        } else {
                            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                            listener.onFail(context.getString(R.string.orderNum) + rechargeOrder.getNumber_game() + context.getString(R.string.pay_failed) + payResult.getMemo());
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        this.rechargeOrder = rechargeOrder;
        Log.d(TAG, "订单信息==" + rechargeOrder.toString());

        commodity = findViewById(R.id.Commodity);
        comDetails = findViewById(R.id.ComDetails);
        rv = findViewById(R.id.recharge_rv);
        btn = findViewById(R.id.popup_submit);
        adapter = new RechargeAdapter(context, lists, position -> {
            pos = position;
            btn.setEnabled(true);
        });
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        rv.setAdapter(adapter);
        commodity.setText(rechargeOrder.getProps_name());
        comDetails.setText("￥" + StrUtil.changeF2Y(String.valueOf(rechargeOrder.getMoney())));
        btn.setOnClickListener(v -> {
            new Thread(() -> {
                if (pos == 1) {
                    charge(true);
                } else {
                    wxCharge();
                }
            }).start();
            dismiss();
        });
    }


    //支付宝支付
    private void charge() {
        try {
            Response<ResponseBody> execute = RetrofitManager.getInstance(getContext()).getApiService().CreateOrder(rechargeOrder.getNumber_game(),
                    rechargeOrder.getMoney(), rechargeOrder.getProps_name(), rechargeOrder.getServer_id(),
                    rechargeOrder.getServer_name(), rechargeOrder.getRole_id(), rechargeOrder.getRole_name(), rechargeOrder.getCallback_url(),
                    rechargeOrder.getExtend_data()).execute();
            JSONObject json = FileUtil.getResponseBody(execute.body());
            if (json.get("code").toString().equals("0")) {
                OrderBean response = JSONObject.toJavaObject(json, OrderBean.class);
                orderNum = response.getData().getNumber();
                Response<ResponseBody> aliExecute = RetrofitManager.getInstance(getContext()).getApiService().AliPay(orderNum).execute();
                JSONObject aliJson = FileUtil.getResponseBody(aliExecute.body());
                if (aliJson.get("code").toString().equals("0")) {
                    AliPayBean aliResponse = JSONObject.toJavaObject(aliJson, AliPayBean.class);
                    String content = aliResponse.getData().getContent();
                    PayTask alipay = new PayTask(context);
                    Map<String, String> result = alipay.payV2(content, true);
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                    Log.i("msp", result.toString());
                } else if (aliJson.get("code").toString().equals("1")) {
                    Object data = aliJson.get("data");
                    JSONObject jsonObject = JSONObject.parseObject(data.toString());
                    Object tip = jsonObject.get("tip");
                    Object link = jsonObject.get("link");
                    Looper.prepare();
                    RechargeDialog rechargeDialog = new RechargeDialog(context, aliJson.get("message").toString(), tip.toString(), link.toString());
                    rechargeDialog.show();
                    Looper.loop();
                } else {
                    listener.onFail(aliJson.get("message").toString());
                }
            } else if (json.get("code").toString().equals("1")) {
                Looper.prepare();
                RechargeDialog rechargeDialog = new RechargeDialog(context, json.get("message").toString().replaceAll("\\\\n", "\n"));
                rechargeDialog.show();
                Looper.loop();
            } else {
                listener.onFail(json.get("message").toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //支付宝支付
    private void charge(boolean exception) {
        try {
            Response<ResponseBody> execute = RetrofitManager.getInstance(getContext()).getApiService().CreateOrder(rechargeOrder.getNumber_game(),
                    rechargeOrder.getMoney(), rechargeOrder.getProps_name(), rechargeOrder.getServer_id(),
                    rechargeOrder.getServer_name(), rechargeOrder.getRole_id(), rechargeOrder.getRole_name(), rechargeOrder.getCallback_url(),
                    rechargeOrder.getExtend_data()).execute();
            JSONObject json = FileUtil.getResponseBody(execute.body());
            if (json.get("code").toString().equals("0")) {
                OrderBean response = JSONObject.toJavaObject(json, OrderBean.class);
                orderNum = response.getData().getNumber();
                Response<ResponseBody> aliExecute = RetrofitManager.getInstance(getContext()).getApiService().AliPay(orderNum, true).execute();
                JSONObject aliJson = FileUtil.getResponseBody(aliExecute.body());
                if (aliJson.get("code").toString().equals("0")) {
                    AliPayBean aliResponse = JSONObject.toJavaObject(aliJson, AliPayBean.class);
                    String content = aliResponse.getData().getContent();
                    PayTask alipay = new PayTask(context);
                    Map<String, String> result = alipay.payV2(content, true);
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                    Log.i("msp", result.toString());
                } else if (aliJson.get("code").toString().equals("1")) {
                    Object data = aliJson.get("data");
                    JSONObject jsonObject = JSONObject.parseObject(data.toString());
                    Object tip = jsonObject.get("tip");
                    Object link = jsonObject.get("link");
                    Looper.prepare();
                    RechargeDialog rechargeDialog = new RechargeDialog(context, aliJson.get("message").toString(), tip.toString(), link.toString());
                    rechargeDialog.show();
                    Looper.loop();
                } else {
                    Looper.prepare();
                    Toast.makeText(context, aliJson.get("message").toString(), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            } else if (json.get("code").toString().equals("1")) {
                Looper.prepare();
                RechargeDialog rechargeDialog = new RechargeDialog(context, json.get("message").toString().replaceAll("\\\\n", "\n"));
                rechargeDialog.show();
                Looper.loop();
            } else {
                Toast.makeText(context, json.get("message").toString(), Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
