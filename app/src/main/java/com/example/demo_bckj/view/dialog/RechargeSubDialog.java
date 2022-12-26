package com.example.demo_bckj.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.example.demo_bckj.R;
import com.example.demo_bckj.model.MyCallback;
import com.example.demo_bckj.model.RetrofitManager;
import com.example.demo_bckj.view.adapter.RechargeAdapter;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;

/**
 * @author ZJL
 * @date 2022/12/16 11:03
 * @des 支付选择弹窗
 * @updateAuthor
 * @updateDes
 */
public class RechargeSubDialog extends Dialog{

    private Context context;
    private TextView commodity,comDetails;
    private RecyclerView rv;
    private RechargeAdapter adapter;
    private Button btn;
    private List<String> lists= Arrays.asList("支付宝","微信");
    private String number_game="",props_name="",server_id="",server_name="",role_id="",role_name="",callback_url="",extend_data="";
    private int money;

    public RechargeSubDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_recharge_sub);
        this.context=context;

        commodity=findViewById(R.id.Commodity);
        comDetails=findViewById(R.id.ComDetails);
        rv=findViewById(R.id.recharge_rv);
        btn=findViewById(R.id.popup_submit);
        adapter=new RechargeAdapter(context,lists);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.addItemDecoration(new DividerItemDecoration(context,LinearLayoutManager.VERTICAL));
        rv.setAdapter(adapter);
        commodity.setText("首充28元");
        comDetails.setText("￥28");
        btn.setOnClickListener(v->{
            dismiss();
        });
    }

    private void charge(){
        RetrofitManager.getInstance(getContext()).getApiService().CreateOrder(number_game,money,props_name,server_id,
                                        server_name,role_id,role_name,callback_url,extend_data).enqueue(new MyCallback<ResponseBody>() {
            @Override
            public void onSuccess(JSONObject jsStr) {

            }

            @Override
            public void onError(String message) {

            }
        });
    }

}
