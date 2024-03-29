package com.bc.sdk.model;

/**
 * @author ZJL
 * @date 2022/12/20 11:26
 * @des
 * @updateAuthor
 * @updateDes
 */

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.bc.sdk.manager.DBManager;
import com.bc.sdk.model.utility.FileUtil;
import com.bc.sdk.model.utility.StrUtil;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Response;


/**
 *
 */

public abstract class MyCallback<ResponseBody extends okhttp3.ResponseBody> implements retrofit2.Callback<ResponseBody> {
    private static final String TAG = "MyCallback";
    private Context mContext;

    public abstract void onSuccess(JSONObject jsStr);

    public abstract void onError(String message);

    public MyCallback() {

    }

    public MyCallback(Context mContext, String progressTitle) {
    }

    public MyCallback(Context mContext) {
        this.mContext = mContext;
    }

    public void setContext(Context context){
        this.mContext=context;
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response == null || response.body() == null) {
            onFailure(call, new Throwable(StrUtil.httpCode(response.code())));
            return;
        }
        Headers headers = response.headers();
        String Authorization = headers.get("Authorization");
        if (mContext != null && !TextUtils.isEmpty(Authorization)) {
            DBManager.getInstance(mContext).insertAuthorization(Authorization);
        }
        JSONObject json = FileUtil.getResponseBody(response.body());
        Object code = json.get("code");
        if (code.toString().equals("0")) {
            onSuccess(json);
        } else {
            onFailure(call, new Throwable(json.getString("message").toString()));
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.d(TAG, "onFailure: ");
        onError(t.getMessage());
    }
}