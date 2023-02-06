package com.bc.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.demo_bckj.control.SDKListener;
import com.example.demo_bckj.control.SdkControl;
import com.example.demo_bckj.model.MyCallback;
import com.example.demo_bckj.model.bean.RechargeOrder;
import com.example.demo_bckj.model.bean.RoleBean;
import com.example.demo_bckj.model.bean.User;
import com.example.demo_bckj.view.fragment.HomeFragment;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";
    SDKListener listener = new SDKListener() {
        @Override
        public void Login(User user) {
            Log.d(TAG, "登录==" + user);
        }

        @Override
        public void SignOut() {
            Log.d(TAG, "退出");
        }

        @Override
        public void RechargeSuccess(String orderNum) {
            Log.d(TAG, orderNum + "充值成功");
        }

        @Override
        public void RechargeFail(String message) {
            Log.d(TAG, "充值失败==" + message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Map<String ,String > map=new HashMap<>();
        map.put("package","65767249bd2a4fb2a4e195fe59aa54aa");

        SdkControl.getInstance(this).init(map);
//        SdkControl.getInstance(this).init(null);
        FragmentManager fm = getSupportFragmentManager();
        HomeFragment homeFragment = HomeFragment.getInstance(listener);
        fm.beginTransaction()
                .replace(R.id.home, homeFragment)
                .addToBackStack(null)
                .commit();

        Button p = findViewById(R.id.pay);
        Button l = findViewById(R.id.loginServer);
        Button c = findViewById(R.id.createRole);

        p.setOnClickListener(v -> {
            SdkControl.getInstance(this).Recharge(this, listener, new RechargeOrder.Builder()
                    .number_game("游戏订单号")
                    .props_name("物品名称")
                    .server_id("区服 ID")
                    .server_name("区服名称")
                    .role_id("角色 ID")
                    .role_name("角色名称")
                    .callback_url("https://apitest.infinite-game.cn/ping")
                    .money(1)
                    .extend_data("")
                    .build());
        });
        c.setOnClickListener(v -> {
            SdkControl.getInstance(this).CreateRole(this, new RoleBean.Builder()
                    .serverID("builder.serverID")
                    .serverName("builder.serverName")
                    .roleId("builder.roleId")
                    .roleName("builder.roleName").bulid(), new MyCallback<ResponseBody>() {
                public void onSuccess(JSONObject jsStr) {
                    Toast.makeText(MainActivity.this, jsStr.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(MainActivity.this, "失败" + message, Toast.LENGTH_SHORT).show();
                }
            });
        });
        l.setOnClickListener(v -> {
            SdkControl.getInstance(this).LoginServer(this, new RoleBean.Builder()
                    .serverID("builder.serverID")
                    .serverName("builder.serverName")
                    .roleId("builder.roleId")
                    .roleName("builder.roleName").bulid(), new MyCallback<ResponseBody>() {
                public void onSuccess(JSONObject jsStr) {
                    Toast.makeText(MainActivity.this, jsStr.toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(MainActivity.this, "失败" + message, Toast.LENGTH_SHORT).show();
                }
            });
        });
        Button b = findViewById(R.id.payException);
        b.setOnClickListener(v -> {
            SdkControl.getInstance(this).Recharge(this, listener, true);
        });

    }
}