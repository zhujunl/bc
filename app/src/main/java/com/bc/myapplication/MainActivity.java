package com.bc.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.alibaba.fastjson.JSONObject;
import com.example.demo_bckj.control.LoginListener;
import com.example.demo_bckj.control.LoginOutListener;
import com.example.demo_bckj.control.RechargeListener;
import com.example.demo_bckj.control.SDKManager;
import com.example.demo_bckj.model.MyCallback;
import com.example.demo_bckj.model.bean.RechargeOrder;
import com.example.demo_bckj.model.bean.RoleBean;
import com.example.demo_bckj.model.bean.User;
import com.example.demo_bckj.view.fragment.HomeFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SDKManager.getInstance().init(MainActivity.this, "");
        FragmentManager fm = getSupportFragmentManager();
        HomeFragment homeFragment = HomeFragment.getInstance(new LoginListener() {
            @Override
            public void onSuccess(User user) {
                Log.d(TAG, "default login success");
            }

            @Override
            public void onFail(String message) {
                Log.d(TAG, "default login fail");
            }
        }, new LoginOutListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "default sign out success");
            }

            @Override
            public void onFail(String message) {
                Log.d(TAG, "default sign out fail");
            }
        });
        fm.beginTransaction()
                .replace(R.id.home, homeFragment)
                .addToBackStack(null)
                .commit();

        Button p = findViewById(R.id.pay);
        Button l = findViewById(R.id.loginServer);
        Button c = findViewById(R.id.createRole);
        Button login = findViewById(R.id.login_account);
        Button out = findViewById(R.id.loginOut);

        p.setOnClickListener(v -> {
            SDKManager.getInstance().Recharge(MainActivity.this, new RechargeOrder.Builder()
                    .number_game("游戏订单号")
                    .props_name("物品名称")
                    .server_id("区服 ID")
                    .server_name("区服名称")
                    .role_id("角色 ID")
                    .role_name("角色名称")
                    .callback_url("https://apitest.infinite-game.cn/ping")
                    .money(1)
                    .extend_data("")
                    .build(), new RechargeListener() {
                @Override
                public void onSuccess(String orderNum) {

                }

                @Override
                public void onFail(String message) {

                }
            });
        });
        c.setOnClickListener(v -> {
            SDKManager.getInstance().CreateRole(MainActivity.this, new RoleBean.Builder()
                    .serverID("builder.serverID")
                    .serverName("builder.serverName")
                    .roleId("builder.roleId")
                    .roleName("builder.roleName").bulid(), new MyCallback<ResponseBody>() {
                public void onSuccess(JSONObject jsStr) {
                    Log.d("CreateRole", "onSuccess:" + jsStr.toString());
                }

                @Override
                public void onError(String message) {
                    Log.d("CreateRole", "onError:" + message);
                }
            });
        });
        l.setOnClickListener(v -> {
            SDKManager.getInstance().LoginServer(MainActivity.this, new RoleBean.Builder()
                    .serverID("builder.serverID")
                    .serverName("builder.serverName")
                    .roleId("builder.roleId")
                    .roleName("builder.roleName").bulid(), new MyCallback<ResponseBody>() {
                public void onSuccess(JSONObject jsStr) {
                    Log.d("LoginServer", "onSuccess:" + jsStr.toString());
                }

                @Override
                public void onError(String message) {
                    Log.d("LoginServer", "onError:" + message);
                }
            });
        });
        Button b = findViewById(R.id.payException);
        b.setOnClickListener(v -> {
            SDKManager.getInstance().Recharge(this, true, new RechargeListener() {
                @Override
                public void onSuccess(String orderNum) {

                }

                @Override
                public void onFail(String message) {

                }
            });
        });

        login.setOnClickListener(v -> SDKManager.getInstance().Login(MainActivity.this, new LoginListener() {
            @Override
            public void onSuccess(User user) {

            }

            @Override
            public void onFail(String message) {

            }
        }));

        out.setOnClickListener(v -> SDKManager.getInstance().LoginOut(false, false, new LoginOutListener() {
            @Override
            public void onSuccess() {
                Log.d("out", "onSuccess");
            }

            @Override
            public void onFail(String message) {
                Log.d("out", "onFail:" + message);
            }
        }));
    }


}