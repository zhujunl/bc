package com.bc.sdk.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bc.sdk.db.entity.ConfigEntity;
import com.bc.sdk.db.MyDBHelper;

/**
 * @author ZJL
 * @date 2023/1/12 9:59
 * @des
 * @updateAuthor
 * @updateDes
 */
public class ConfigDao {
    private MyDBHelper mHelper;
    private SQLiteDatabase mDatabase;

    private static ConfigDao instance;

    public static ConfigDao getInstance(Context context) {
        if (instance == null) {
            instance = new ConfigDao(context);
        }
        return instance;
    }


    public ConfigDao(Context context) {
        mHelper = MyDBHelper.getInstance(context);
        mDatabase = mHelper.getWritableDatabase();
    }

    public void insertAuthorization(String authorization) {
        deleteAuthorization();
        ContentValues values = new ContentValues();
        values.put("authorization", authorization);
        mDatabase.insert("config", null, values);
    }

    public void deleteAuthorization() {
        mDatabase.delete("config", "id > 0", new String[]{});
    }

    public ConfigEntity queryAuthorization() {
        String sql = "select * from config";
        Cursor cursor = mDatabase.rawQuery(sql, null);
        if (cursor == null) {
            return null;
        }
        while (cursor.moveToNext()) {
            ConfigEntity config = new ConfigEntity(cursor.getString(1));
            return config;
        }
        return null;
    }
}
