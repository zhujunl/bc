package com.bc.sdk.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.bc.sdk.manager.HttpManager;

import androidx.annotation.Nullable;

/**
 * @author ZJL
 * @date 2023/1/11 10:38
 * @des
 * @updateAuthor
 * @updateDes
 */
public class TimeService extends IntentService {
    private final String TAG = "TimeService";
    private Context context;
    private static boolean run;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * <p>
     * Used to name the worker thread, important only for debugging.
     */
    public TimeService() {
        super("TimeService");
    }

    public static void start(Context context) {
        run=true;
        Intent intent = new Intent(context, TimeService.class);
        context.startService(intent);
        Log.d("TimeService", "startService");
    }

    public static void stop(Context context){
        run=false;
        Log.d("TimeService", "stopService");
    }

    public static boolean isRun() {
        return run;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null)
            return;
        while (run) {
            Log.d(TAG, "Service  onHandlerIntent");
            HttpManager.getInstance().isOnline(this);
            SystemClock.sleep(60000);
        }
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }
}
