package com.bc.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.demo_bckj.control.GameCallBack;
import com.example.demo_bckj.control.LoginCallBack;
import com.example.demo_bckj.control.LoginOutCallBack;
import com.example.demo_bckj.control.RechargeCallBack;
import com.example.demo_bckj.control.SDKManager;
import com.example.demo_bckj.model.bean.RechargeOrder;
import com.example.demo_bckj.model.bean.RoleBean;
import com.example.demo_bckj.model.bean.User;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity===";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");

        SDKManager.getInstance().init(MainActivity.this, "", new LoginCallBack() {
            @Override
            public void onSuccess(User user) {
                Log.d(TAG, "init login success" );
            }

            @Override
            public void onFail(String message) {
                Log.d(TAG, "init login fail" );
            }
        }, new LoginOutCallBack() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "init login out success" );
            }

            @Override
            public void onFail(String message) {
                Log.d(TAG, "init login out fail" );
            }
        });

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
                    .build(), new RechargeCallBack() {
                @Override
                public void onSuccess(String orderNum) {
                    Log.d(TAG, "支付成功" );
                }

                @Override
                public void onFail(String message) {
                    Log.d(TAG, "支付失败" );
                }
            });
        });
        c.setOnClickListener(v -> {
            SDKManager.getInstance().CreateRole(MainActivity.this, new RoleBean.Builder()
                    .serverID("builder.serverID")
                    .serverName("builder.serverName")
                    .roleId("builder.roleId")
                    .roleName("builder.roleName").bulid(), new GameCallBack() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "CreateRole Success");
                }

                @Override
                public void onFail(String message) {
                    Log.d(TAG, "CreateRole Fail");
                }
            });
        });
        l.setOnClickListener(v -> {
            SDKManager.getInstance().LoginServer(MainActivity.this, new RoleBean.Builder()
                    .serverID("builder.serverID")
                    .serverName("builder.serverName")
                    .roleId("builder.roleId")
                    .roleName("builder.roleName").bulid(), new GameCallBack() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "LoginServer Success");
                }

                @Override
                public void onFail(String message) {
                    Log.d(TAG, "LoginServer Fail");
                }
            });
        });

        login.setOnClickListener(v -> SDKManager.getInstance().Login(MainActivity.this, null));

        out.setOnClickListener(v -> SDKManager.getInstance().LoginOut(false, false, null));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart" );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume" );
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause" );
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop" );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        SDKManager.getInstance().cancellation();
    }
}