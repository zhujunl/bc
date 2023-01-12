package com.example.demo_bckj.control;

import android.content.Context;

import com.example.demo_bckj.manager.HttpManager;
import com.example.demo_bckj.model.bean.RoleBean;

/**
 * @author ZJL
 * @date 2023/1/12 17:10
 * @des
 * @updateAuthor
 * @updateDes
 */
public class SdkControl {

    private static SdkControl instance;
    private Context context;

    public static SdkControl getInstance(Context context){
        if (instance==null){
            instance=new SdkControl(context);
        }
        return instance;
    }

    public SdkControl(Context context) {
        this.context=context;
    }

    //用户创角
    public void CreateRole(Context context, RoleBean roleBean) {
        HttpManager.getInstance().CreateRole(context,roleBean);
    }

    //角色登录区服
    public void LoginServer(Context context, RoleBean roleBean){
        HttpManager.getInstance().LoginServer(context,roleBean);
    }
}
