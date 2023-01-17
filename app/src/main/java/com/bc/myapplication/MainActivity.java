package com.bc.myapplication;

import android.os.Bundle;
import android.util.Log;

import com.example.demo_bckj.control.SDKListener;
import com.example.demo_bckj.control.SdkControl;
import com.example.demo_bckj.model.bean.User;
import com.example.demo_bckj.view.fragment.HomeFragment;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

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
        FragmentManager fm = getSupportFragmentManager();
        HomeFragment homeFragment = HomeFragment.getInstance(listener);
        fm.beginTransaction()
                .replace(R.id.home, homeFragment)
                .addToBackStack(null)
                .commit();

    }


}