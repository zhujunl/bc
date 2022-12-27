package com.example.demo_bckj.manager;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * @author ZJL
 * @date 2022/12/27 13:33
 * @des
 * @updateAuthor
 * @updateDes
 */
public class ActivityManager {

    private WeakReference<Activity> sCurrentActivityWeakRef;


    private static volatile ActivityManager singleton;

    private ActivityManager() {
    }

    public static ActivityManager getInstance() {
        if (singleton == null) {
            synchronized (ActivityManager.class) {
                if (singleton == null) {
                    singleton = new ActivityManager();
                }
            }
        }
        return singleton;
    }

    public Activity getCurrentActivity() {
        Activity currentActivity = null;
        if (sCurrentActivityWeakRef != null) {
            currentActivity = sCurrentActivityWeakRef.get();
        }
        return currentActivity;
    }

    public void setCurrentActivity(Activity activity) {
        sCurrentActivityWeakRef = new WeakReference<Activity>(activity);
    }


}