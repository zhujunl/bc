package com.bc.sdk.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.bc.sdk.R;
import com.bc.sdk.control.RechargeCallBack;
import com.bc.sdk.manager.HttpManager;
import com.bc.sdk.model.bean.RechargeOrder;
import com.bc.sdk.model.utility.StrUtil;
import com.bc.sdk.view.adapter.RechargeAdapter;

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
    private TextView commodity, comDetails;
    private RecyclerView rv;
    private RechargeAdapter adapter;
    private Button btn;
    private List<String> lists = Arrays.asList("微信", "支付宝");
    private int pos;
    private RechargeCallBack listener;

    public RechargeSubDialog(@NonNull Activity context, RechargeOrder rechargeOrder, RechargeCallBack listener) {
        super(context);
        setContentView(R.layout.dialog_recharge_sub);
        this.listener = listener;
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
                    HttpManager.getInstance().WXCharge(context,rechargeOrder,listener);
                }
                dismiss();
            }).start();
        });
    }
}
