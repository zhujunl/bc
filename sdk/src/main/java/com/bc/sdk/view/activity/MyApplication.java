package com.bc.sdk.view.activity;

import android.app.Application;

import com.bc.sdk.crash.CrashHandler;

import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.unit.Subunits;

/**
 * @author WangKun
 * @description:
 * @date :2022/10/20 16:48
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //自动尺寸配置  对单位的自定义配置, 在 App 启动时完成
        AutoSizeConfig.getInstance().getUnitsManager()
                .setSupportDP(false)
                .setSupportSP(false)
                .setSupportSubunits(Subunits.MM);

            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(getApplicationContext());

    }
}
