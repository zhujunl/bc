package com.example.demo_bckj.view.activity;

import android.widget.PopupWindow;

/**
 * @author WangKun
 * @description:
 * @date :2022/10/26 11:36
 */
public class HookPopupWindow extends PopupWindow {
    public boolean canDismiss = true;// 设为false可以控制dismiss()无参方法不调用 主要是为了点击PopupWindow外部不消失


    @Override
    public void dismiss() {
//        new Exception().printStackTrace();
        if(canDismiss)
            dismiss2();
        else {
            StackTraceElement[] stackTrace = new Exception().getStackTrace();
            if(stackTrace.length >= 2 && "dispatchKeyEvent".equals(stackTrace[1].getMethodName())){
                dismiss2();
            }
        }
    }

    public void dismiss2(){
        super.dismiss();
    }
}

