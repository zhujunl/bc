package com.example.demo_bckj.view.activity;

import android.os.Bundle;
import android.util.Log;

import com.example.demo_bckj.R;
import com.example.demo_bckj.listener.SDKListener;
import com.example.demo_bckj.model.bean.Account;
import com.example.demo_bckj.view.fragment.HomeFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity{
    String TAG="MainActivity";
    SDKListener listener=new SDKListener() {
        @Override
        public void Login(Account account) {
            Log.d(TAG, "登录=="+account );
        }

        @Override
        public void SignOut() {
            Log.d(TAG, "退出" );
        }

        @Override
        public void BindNewPhone(Account account) {
            Log.d(TAG, "换绑=="+account );
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        HomeFragment homeFragment=HomeFragment.getInstance(listener);
        fm.beginTransaction()
                .replace(R.id.home,homeFragment)
                .addToBackStack(null)
                .commit();
    }



}